package entity;

import java.util.Set;

public class Librarian extends BaseEntity{
    private String fullName;
    private String email;
    private String password;
    private Set<Responsibility> responsibilities;

    public Librarian(String fullName, String email, String password, Set<Responsibility> responsibilities) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.responsibilities = responsibilities;
    }


    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Responsibility> getResponsibility() {
        return responsibilities;
    }

    public boolean hasResponsibility(Responsibility responsibility) {
        return responsibilities != null && responsibilities.contains(responsibility);
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", responsibilities=" + responsibilities +
                '}';
    }
}
