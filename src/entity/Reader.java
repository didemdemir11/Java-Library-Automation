package entity;

import java.util.List;

public class Reader extends BaseEntity{
    private String fullName;
    private String email;
    private String password;
    private List<Book> borrowedBooks;

    public Reader(String fullName, String email, String password,List<Book> borrowedBooks) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.borrowedBooks = borrowedBooks;
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
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
