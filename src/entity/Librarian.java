package entity;

import java.util.Set;

public class Librarian extends BaseEntity{
    private String name;
    private String email;
    private String password;
    private Responsibility responsibility;

    public Librarian(String name, String email, String password, Responsibility responsibility) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.responsibility = responsibility;
    }

    public Librarian(String admin, Set<Responsibility> collect) {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Responsibility getResponsibility() {
        return responsibility;
    }
}
