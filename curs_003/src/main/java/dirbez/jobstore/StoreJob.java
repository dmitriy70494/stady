package dirbez.jobstore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс Занимается записьсью и выводом данных из БД
 */
public class StoreJob implements Closeable {

    /**
     * Логер, записывает все данные произошедшие в методах в лог файлы
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(StoreJob.class);

    /**
     * Класс с файлами настроек и команд
     */
    private Config config;

    /**
     * список доступных команд
     */
    private String[] commands = new String[]{"0_url", "1_url", "2_username", "3_password", "4_check_database",
            "5_greate_database", "6_number_last_command_for_create_table_in_database", "7_check_table_employer", "8_create_table_employer",
            "9_check_table_employer", "10_create_table_employer", "11_insert_in_employer", "12_insert_in_job", "13_count_start_hours",
            "14_select_last_date", "15_select_job_and_employer", "16_select_employer", "17_select_for_logging"};

    /**
     * Соединение с БД
     */
    private Connection connection;


    /**
     * Конструктор
     *
     * @param config
     */
    public StoreJob(Config config) {
        this.config = config;
    }

    /**
     * Инициализирует подключение, создает таблицы и базы данных
     */
    public void initDatabase() {
        try (Connection connectionRoot = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Lbvbnhbq123");
             Statement statementRoot = connectionRoot.createStatement();) {

            if (!statementRoot.executeQuery("SELECT 1 FROM pg_database WHERE datname = 'ru_sql'").next()) {
                statementRoot.execute("CREATE DATABASE ru_sql");
            }
            connectionRoot.close();
            statementRoot.close();
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ru_sql", "postgres", "Lbvbnhbq123");
            this.connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                if (!statement.executeQuery("SELECT tablename FROM pg_tables WHERE tablename = 'employer'").next()) {
                    statement.execute("CREATE TABLE employer (id serial primary key, name character varying (100), link text)");
                }
                if (!statement.executeQuery("SELECT tablename FROM pg_tables WHERE tablename = 'job'").next()) {
                    statement.execute("CREATE TABLE job (id serial primary key, title text, link text, description text, id_employer integer references employer, dates timestamp)");
                }
                this.connection.commit();
            }
        } catch (SQLException ie) {
            ie.printStackTrace();
            LOGGER.error(ie.getMessage(), ie);
        }
    }

    /**
     * Заполняет таблицу работодателей
     *
     * @param employers
     * @return id таблицы работодателей
     */
    public Queue<Integer> fillEmployer(Queue<String> employers) {
        try (PreparedStatement insertEmployer = this.connection.prepareStatement("INSERT INTO employer (name, link) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            Queue<Integer> result = new LinkedList<>();
            this.connection.setAutoCommit(true);
            while (!employers.isEmpty()) {
                String employer = employers.poll();
                String employerLink = employers.poll();
                int key = this.checkReplayEmployer(employer);
                if (key < 0) {
                    insertEmployer.setString(1, employer);
                    insertEmployer.setString(2, employerLink);
                    insertEmployer.executeUpdate();
                    try (ResultSet keys = insertEmployer.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
                result.offer(key);
            }
            this.connection.setAutoCommit(false);
            return result;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException e1) {
                LOGGER.error(e1.getMessage(), e1);
            }
            LOGGER.error(e.getMessage(), e);
        }
        throw new IllegalStateException("fillEmployer");
    }

    /**
     * проверяет содержание работодателя в таблице по имени, которое уникально
     *
     * @param employer имя
     * @return id имени если есть в таблице
     */
    private int checkReplayEmployer(String employer) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM employer WHERE name = ?")) {
            statement.setString(1, employer);
            int key = -1;
            ResultSet check = statement.executeQuery();
            if (check.next()) {
                key = check.getInt(1);
            }
            return key;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        throw new IllegalStateException("checkReplayEmployer");
    }

    /**
     * Заполняет таблицу работы
     *
     * @param dates даты
     * @param jobs  подходящие работы
     * @param keys  id работодателей так как отношение один ко многим
     */
    public void fillJob(Queue<Timestamp> dates, Queue<String> jobs, Queue<Integer> keys) {
        try (PreparedStatement insertJob = connection.prepareStatement("INSERT INTO job (title, link, description, id_employer, dates) VALUES (?, ?, ?, ?, ?)")) {
            while (!dates.isEmpty()) {
                Integer idEmployer = keys.poll();
                String job = jobs.poll();
                String link = jobs.poll();
                String desc = jobs.poll();
                Timestamp date = dates.poll();
                if (this.checkReplay(job, idEmployer)) {
                    insertJob.setString(1, job);
                    insertJob.setString(2, link);
                    insertJob.setString(3, desc);
                    insertJob.setInt(4, idEmployer);
                    insertJob.setTimestamp(5, date);
                    insertJob.executeUpdate();
                }
            }
            this.connection.commit();
        } catch (SQLException ie) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
            ie.printStackTrace();
            LOGGER.error(ie.getMessage(), ie);
        }
    }

    /**
     * Проверяет есть ли работа в базе, по назаванию и работодателю
     *
     * @param job        работа
     * @param idEmployer id работодателя
     * @return
     */
    private boolean checkReplay(String job, int idEmployer) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM job WHERE title = ? AND id_employer = ?")) {
            statement.setString(1, job);
            statement.setInt(2, idEmployer);
            try (ResultSet check = statement.executeQuery()) {
                return !check.next();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        throw new IllegalStateException("checkReplay");
    }


    /**
     * самую последнюю дату в таблице
     *
     * @return дата последнего заполнения
     */
    public Timestamp getTimeLastJob() {
        try (Statement statement = this.connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT MAX(dates) FROM job");) {
            Timestamp date = null;
            if (result.next()) {
                date = result.getTimestamp(1);
            }
            if (date == null || date.getTime() < 10000) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(calendar.get(1), 0, 1, 0, 0);
                date = new Timestamp(calendar.getTimeInMillis());
            }
            return date;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        throw new IllegalStateException("time not set");
    }

    public String stringLogging(Timestamp stopDate) {
        try (PreparedStatement statement = this.connection.prepareStatement("SELECT title, a.link, description, name, b.link, dates FROM job AS a INNER JOIN employer AS b ON a.id_employer = b.id WHERE dates > ? ORDER BY dates")) {
            StringBuilder result = new StringBuilder();
            statement.setTimestamp(1, stopDate);
            try (ResultSet jobs = statement.executeQuery()) {
                while (jobs.next()) {
                    result.append(jobs.getString(1) + System.lineSeparator())
                            .append(jobs.getString(2) + System.lineSeparator())
                            .append(jobs.getString(3) + System.lineSeparator())
                            .append(jobs.getString(4) + System.lineSeparator())
                            .append(jobs.getString(5) + System.lineSeparator())
                            .append(jobs.getTimestamp(6).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MM uu, HH:mm", new Locale("ru", "RU"))) + System.lineSeparator())
                            .append("------------------------------------------------" + System.lineSeparator());
                }
            }
            return result.toString();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        throw new IllegalStateException("Don't logging");
    }


    /**
     * Закрывает соединение с БД
     */
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
