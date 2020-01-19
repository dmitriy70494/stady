package dirbez.jobstore;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Запускает и управляет приложением в целом и его модулями
 */
public class StartUI {

    private static final org.apache.logging.log4j.Logger ROOT_LOGGER = LogManager.getRootLogger();

    /**
     * Логер, записывает все данные произошедшие в методах в лог файлы
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(StoreJob.class);


    /**
     * запускает бесконечный цикл для главного потока, частоту запуска можно регулировать в конфигурационном файле
     */
    public void init() {
        try {
            while (true) {
                long sleep = this.start();
                Thread.sleep((sleep) * 60 * 60 * 1000);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Инициализирует все необходмые для работы объекты, добавляет их в 2 потока возвращает главному потоку врямя сна, главный поток засыпает
     *
     * @return время ожидания следующего запуска
     */
    public long start() {
        Config config = new Config().initialConfig("C:/Projects/study/study/chapter_002/ru_sql.sql");
        StoreJob store = new StoreJob(config);
        store.initDatabase();
        Timestamp stopDate = store.getTimeLastJob();
        ConcurrentLinkedQueue<Jobs> readed = new ConcurrentLinkedQueue<Jobs>();
        ConcurrentLinkedQueue<Jobs> writed = new ConcurrentLinkedQueue<Jobs>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uu, HH:mm");
        readed.offer(new Jobs(formatter, stopDate));
        readed.offer(new Jobs(formatter, stopDate));
        Thread writer = new WriteJob(readed, writed, store, stopDate);
        new ReadJob(readed, writed, writer, "http://www.sql.ru/forum/job/").start();
        writer.start();
        return 10L;//поправить
    }

    /**
     * Записывает программу в реестр windows, подходит не для всех, в программе не вызывается, на Unix и Mac не сработает
     */
    public void startFirst() {
        try {
            File file = new File("java/class/StartUI.class");
            String write = String.format(
                    "cmd /C " + "reg add HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /v %s /t REG_SZ /d \"%s\"", file.getName(), file
            );
            Runtime.getRuntime().exec(write);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * запускает приложение
     *
     * @param args
     */
    public static void main(String[] args) {
        new StartUI().init();
    }

/**
 * первая нить, читает сайт, заносит информацию в первый объект, передает его на запись второму потоку
 */
class ReadJob extends Thread {

    /**
     * Очередь на запись, если пуста, поток ждет пока не освободится объект
     */
    ConcurrentLinkedQueue<Jobs> readed;

    /**
     * очередь на запись в БД, обработанные объекты передаются сюда
     */
    ConcurrentLinkedQueue<Jobs> writed;

    /**
     * ссылка на страницу, лучше было сделать arraylist, и заполнять уже в главном методе, так проще управлять потом будет, когда вся
     * конфигурационнная информация в одном месте
     */
    String url;

    /**
     * ссылка на второй поток для прерывания
     */
    Thread writer;

    /**
     * индикатор продолжения работы, зависит от обработки даты, если даты в объекте не подходящие, то объект предупреждает об этом
     * хотя это может быть ахиллесовой пятой, так как первые комменты старых дат, благо сначала идет проверка содержимого, поэтому
     * проблемы не возникает
     */
    boolean worked = true;

    /**
     * все работы на сайте содержат указатель class с именем "postslisttopic", оно передается для парсинга элементов
     */
    static final String CLASS_JOB = "postslisttopic";

    /**
     * все даты и работодатели на сайте содержат указатель class с именем "altCol", оно передается для парсинга элементов
     */
    static final String CLASS_DATE_AND_EMPLOYER = "altCol";

    /**
     * Конструткор
     *
     * @param readed
     * @param writed
     * @param writer
     * @param url
     */
    public ReadJob(ConcurrentLinkedQueue<Jobs> readed, ConcurrentLinkedQueue<Jobs> writed, Thread writer, String url) {
        this.readed = readed;
        this.writed = writed;
        this.url = url;
        this.writer = writer;
    }

    /**
     * берет из очереди объект, парсит url добавляет обработанный объект во вторую очередь, индек обозначает
     * следующую страничку, так как на сайте они тоже индексируются
     */
    @Override
    public void run() {
        int index = 1;
        while (worked) {
            ParserJob parser = new ParserJob();
            Jobs jobs = this.readed.poll();
            if (jobs != null) {
                jobs.remove();
                jobs.addJobAndLinkJob(parser.parseTableJob(url + index, CLASS_JOB), parser);
                this.worked = jobs.addDateAndEmployer(parser.parseTableJob(url + index++, CLASS_DATE_AND_EMPLOYER));
                writed.offer(jobs);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        if (!writer.isInterrupted()) {
            writer.interrupt();
        }
    }
}

class WriteJob extends Thread {

    /**
     * передает обработанные элементы сюда
     */
    ConcurrentLinkedQueue<Jobs> readed;

    /**
     * обрабатывает элементы отсюда
     */
    ConcurrentLinkedQueue<Jobs> writed;

    /**
     * ссылка на хранилище для передачи команд
     */
    StoreJob store;

    Timestamp stopDate;

    /**
     * Конструктор
     *
     * @param readed
     * @param writed
     * @param store
     */
    public WriteJob(ConcurrentLinkedQueue<Jobs> readed, ConcurrentLinkedQueue<Jobs> writed, StoreJob store, Timestamp stopDate) {
        this.readed = readed;
        this.writed = writed;
        this.store = store;
        this.stopDate = stopDate;
    }

    /**
     * работает пока поток не запустит прерывание и очередь не опустеет
     * после происходит запись в лог и обязательно закрывается соединение с БД
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() || writed.size() != 0) {
            Jobs jobs = this.writed.poll();
            if (jobs != null) {
                Queue<Integer> keys = store.fillEmployer(jobs.getEmployer());
                store.fillJob(jobs.getDate(), jobs.getJobs(), keys);
                readed.offer(jobs);
            }
        }
        store.close();
    }
}
}

