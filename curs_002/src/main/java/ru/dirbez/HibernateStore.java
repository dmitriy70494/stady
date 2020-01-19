package ru.dirbez;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class HibernateStore implements Closeable {

    private final static HibernateStore instance = new HibernateStore();

    private SessionFactory factory;

    private HibernateStore() {
        Configuration conf = new Configuration()
                .configure();
        conf.addAnnotatedClass(Client.class);
        conf.addAnnotatedClass(Task.class);
        this.factory =
                conf.buildSessionFactory();
    }

    public static HibernateStore getInstance() {
        return instance;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = this.factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            return command.apply(session);
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.commit();
            }
            session.close();
        }
    }

    public <T> void add(final T item) {
        this.tx(
                session -> {
                    session.save(item);
                    return null;
                }
        );
    }

    public void update(Task item) {
        this.tx(
                session -> {
                    session.update(item);
                    return null;
                }
        );
    }

    public void delete(final Task item) {
        this.tx(
                session -> {
                    session.delete(item);
                    return null;
                }
        );
    }

    public List findAll() {
        return this.tx(
                session -> {
                    return session.createQuery("from Task").list();
                }
        );
    }

    public List findAllClient() {
        return this.tx(
                session -> {
                    return session.createQuery("from Client").list();
                }
        );
    }

    public Task testPersistent() {
        final Session session = this.factory.openSession();
        final Transaction tx = session.beginTransaction();
        Task item1 = (Task) session.get(Task.class, 17);
        session.flush();
        return null;
    }

    @Override
    public void close() throws IOException {
        this.factory.close();
    }
}
