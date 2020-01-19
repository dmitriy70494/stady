package dirbez.jobstore;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Хранит кластеры информации, решил сделать хранилищем, так как так меньше тратится ресурсов и времени на создание объектов
 */
public class Jobs {

    /**
     * Дата остановки проверки и поднятия флага
     */
    Timestamp stopDate;

    /**
     * формат для парсинга даты
     */
    DateTimeFormatter formatter;

    /**
     * размер готовых к записи объектов
     */
    private int size = 0;

    /**
     * номера готовых к записи объектов
     */
    private LinkedList<Integer> indexes = new LinkedList<Integer>();

    /**
     * список дат
     */
    private HashMap<Integer, Timestamp> dates = new HashMap<>(100);

    /**
     * ссылка
     */
    private HashMap<Integer, String> linkJob = new HashMap<>(100);

    /**
     * название работы
     */
    private HashMap<Integer, String> job = new HashMap<>(100);

    /**
     * Описание требований
     */
    private HashMap<Integer, String> description = new HashMap<>(100);

    /**
     * ссылка на работодателя
     */
    private HashMap<Integer, String> linkEmployer = new HashMap<>(100);

    /**
     * ник работодателя
     */
    private HashMap<Integer, String> employer = new HashMap<>(100);

    /**
     * конструктор
     * @param formatter
     * @param stopDate
     */
    public Jobs(DateTimeFormatter formatter, Timestamp stopDate) {
        this.formatter = formatter;
        this.stopDate = stopDate;
    }

    /**
     * Добавляет работу и описание, проверяет их на содержание слова java, если содержится javascript или java Script, то
     * данное объяевление не добавляется.
     *
     * @param posts
     * @param parser
     * @return
     */
    public boolean addJobAndLinkJob(Elements posts, ParserJob parser) {
        boolean access = true;
        if (posts == null || parser == null || posts.isEmpty()) {
            access = false;
        }
        for (int index = 0; index < posts.size(); index++) {
            Element job = posts.get(index).select("a").first();
            String work = job.html();
            String link = job.attr("href");
            String description = parser.parseData(link, "msgBody", 1);
            if(this.checkJava(work) || this.checkJava(description)) {
                indexes.add(index);
                this.job.put(index, work);
                this.linkJob.put(index, link);
                this.description.put(index, description);
                this.size++;
            }
        }
        return access;
    }

    /**
     * проверка на соответствие
     *
     * @param check
     * @return
     */
    private boolean checkJava(String check) {
        return check.contains("java") || check.contains("Java") || check.contains("JAVA") && !check.contains("cript");
    }

    /**
     * Добавляет даты только к тем объявлениям, которые содержат java и подходят для записи, можно было сделать наоборот, но
     * мне показалась операция парсинга строки и создание нового объекта более дорогой, тем более что даты отфильтровываться будут лишь один раз
     * @param altCol Элементы с датами и работодателями
     * @return необходимость продолжать работу
     */
    public boolean addDateAndEmployer(Elements altCol) {
        boolean isProceed = true;
        if (this.stopDate == null || altCol == null || altCol.size() == 0) {
            throw new IllegalStateException("dataStop == null || jobs == null" + altCol + "//" + this.stopDate);
        }
        Iterator it = this.indexes.iterator();
        while (it.hasNext()) {
            int index = (Integer) it.next();
            int indexEl = index * 2;
            Elements employer = altCol.get(indexEl++).select("a");
            Timestamp time = new Timestamp(this.parseDate(altCol.get(indexEl).html()));
            if (this.stopDate.before(time)) {
                this.dates.put(index, time);
                this.employer.put(index, employer.html());
                this.linkEmployer.put(index, employer.attr("href"));
            } else {
                isProceed = false;
                it.remove();
                while (it.hasNext()) {
                    it.next();
                    it.remove();
                    this.size--;
                }
            }
        }
        return isProceed;
    }

    /**
     * Парсит дату, в шаблоне есть глюк, он не воспринимает май, парсит только мая, поэтому пришлось поправлять, это
     * костыль, нужно почитать больше про парсинг дат, поискать другие методы, может проще было написать свой парсинг...
     *
     * @param dateString дата
     * @return объект Timestemp даты
     */
    public long parseDate(String dateString) {
        long date;
        dateString = dateString.replace('й', 'я');
        if (dateString.contains("сегодня") || dateString.contains("вчера")) {
            date = System.currentTimeMillis();
        } else {
            date = LocalDateTime.parse(dateString, formatter).toEpochSecond(ZoneOffset.UTC) * 1000;
        }
        return date;
    }

    /**
     * Очередь всех работодателей с линками
     * @return
     */
    public Queue<String> getEmployer() {
        Queue<String> result = new LinkedList<>();
        for(int index : this.indexes) {
            result.offer(this.employer.get(index));
            result.offer(this.linkEmployer.get(index));
        }
        return result;
    }

    /**
     * очередь работ с линками и описаниями, только подходящие
     * @return
     */
    public Queue<String> getJobs() {
        Queue<String> result = new LinkedList<>();
        for(int index : this.indexes) {
            result.offer(this.job.get(index));
            result.offer(this.linkJob.get(index));
            result.offer(this.description.get(index));
        }
        return result;
    }

    /**
     * очередь дат
     * @return
     */
    public Queue<Timestamp> getDate() {
        Queue<Timestamp> result = new LinkedList<>();
        for(int index : this.indexes) {
            result.offer(this.dates.get(index));
        }
        return result;
    }

    /**
     * очищает размер и индексы, остальное не стал чистить, так как это HashMap, то она сама заменит все повторы во время коллизий
     */
    public void remove() {
        this.size = 0;
        this.indexes.clear();
    }

    /**
     * возвращает количество элементов готовых к записи
     * @return
     */
    public int getSize() {
        return this.size;
    }
}
