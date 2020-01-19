package dirbez.jobstore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * обрабатывает странички
 */
public class ParserJob {

    /**
     * парсит нужную страницу, поис по имени class="".
     * @param url ссылка
     * @param classname имя класса
     * @return
     */
    public Elements parseTableJob(String url, String classname) {
        Document document = this.connection(url);
        if (document != null) {
            Elements elements = document.getElementsByClass(classname);
            return elements;
        }
        throw new IllegalArgumentException("No connection with sites, check Internet or this Link: " + url);
    }

    /**
     * Берет всего 1 элемент данного класса
     * @param url ссылка
     * @param classname имя класса
     * @param numberElement номер извлекаемого элемента
     * @return Строковое представление элемента
     */
    public String parseData(String url, String classname, int numberElement) {
        Document document = this.connection(url);
        if (document != null) {
            Elements tables = document.getElementsByClass(classname);
            return tables.get(numberElement).html();
        }
        throw new IllegalArgumentException("No connection with sites, check Internet or this Link: " + url);
    }

    /**
     * Соединение с сайтом, иногда оно падает, поэтому решил реализовать его так, чтобы оно могло 5 раз проверить соединение с сайтом
     * @param url
     * @return
     */
    private Document connection(String url) {
        int index = 0;
        Document document = null;
        boolean access;
        do {
            try {
                document = Jsoup.connect(url).get();
                access = false;
            } catch (IOException e) {
                if (index++ < 5) {
                    access = true;
                    try {
                        Thread.currentThread().join(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                return null;
            }
        } while (access);
        return document;
    }
}
