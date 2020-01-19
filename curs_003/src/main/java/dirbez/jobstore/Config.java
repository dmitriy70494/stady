package dirbez.jobstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Config - объект содержащий настройки для подключения к базе.
 */
public class Config {

    private final static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    /**
     * все запросы, ключи к запросам команды
     */
    private Properties properties = new Properties();

    /**
     * конструктор, объект создает и передает на него ссылку метод initialConfig
     */
    public Config() {
    }

    /**
     * Решил попробовать инициализировать объект не через конструктор, а через статический метод, как рекомендовал Блох,
     * возможно в данном случае это излишне, так как это больше подходит для классов которые нужно создавать по разному для
     * понимания для чего они создвются, как Executore например. Но в этом классе мне необходило было проинициализировать
     * объект, прочитав конфигурацию из файла и создавая объект через конструктор, пришлось бы вызывать метод инициализации
     * иначе бы объект неправильно работал. Наверняка не стоит писать много кода в статическом методе, почитать
     * про это потом подробнее. Если посмотреть в DriverManager, то там все остальные вызовы приходятся на статические методы.
     * Хотя скорее всего там какой то более глобальный паттерн.
     *
     * @param file
     * @return
     * @throws IllegalArgumentException
     */
    public Config initialConfig(String file) {
        try {
            this.properties.load(new FileInputStream(file));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return this;
    }

    public String command(String requestCommand) {
        return this.properties.getProperty(requestCommand);
    }
}