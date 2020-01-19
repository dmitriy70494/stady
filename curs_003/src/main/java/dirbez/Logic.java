package dirbez;

import java.util.List;

public class Logic {
    private HibernateStore store = HibernateStore.getInstance();

    private final static Logic instance = new Logic();

    public static Logic getInstance() {
        return instance;
    }

    public <T> void add(T task) {
        this.store.add(task);
    }

    public List getAll() {
        return this.store.findAll();
    }
}
