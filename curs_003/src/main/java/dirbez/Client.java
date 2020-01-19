package dirbez;

import javax.persistence.*;

@Entity
@Table(name="employer")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "name")
    String name;

    @Column (name = "link")
    String phone;

    public Client() {}

    public Client(Integer id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "название='" + name + '\'' +
                ",\r\n ссылка='" + phone + '\'';
    }
}
