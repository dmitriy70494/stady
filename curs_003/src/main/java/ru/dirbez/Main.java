package ru.dirbez;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        int index = 1;
        Main main = new Main();
        Document document = main.connection("https://line4bet.ru/wp-content/themes/twentyseventeen/action_sport.php", "25-04-2019", null);
        //"https://line4bet.ru/1x-01-01-2019-football/"
        Elements elements = document.getElementsByTag("table");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element table = iterator.next();
            if ("liga".equals(table.attributes().get("class"))) {
                table = iterator.next();
            }
            if ("event".equals(table.attributes().get("class"))) {
                Elements matchs = table.getElementsByTag("td");
                Iterator<Element> matchIterator = matchs.iterator();
                while (matchIterator.hasNext()) {
                    Element match = matchIterator.next();
                }
            }
            System.out.println(iterator.next().text());
            iterator.next().html();
        }
    }

    /**
     * Соединение с сайтом, иногда оно падает, поэтому решил реализовать его так, чтобы оно могло
     * 5 раз проверить соединение с сайтом
     *
     * @param url
     * @return
     */
    private Document connection(String url, String date, String part) {
        int index = 0;
        Document document = null;
        boolean access = false;
        do {
            try {
                if (part == null || part == "") {
                    document = Jsoup.connect("https://line4bet.ru/wp-content/themes/twentyseventeen/action_sport.php")
                            .data("data_p", date)
                            .data("sport_p", "football")
                            .data("buk_p", "1xstavka")
                            .post();
                } else {
                    document = Jsoup.connect("https://line4bet.ru/wp-content/themes/twentyseventeen/action_sport.php")
                            .data("data_p", date)
                            .data("sport_p", "football")
                            .data("buk_p", "1xstavka")
                            .data("par_p", part)
                            .post();
                }
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
            }
        } while (access);
        return document;
    }
}
