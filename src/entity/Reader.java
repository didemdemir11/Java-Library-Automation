package entity;

import java.util.List;

public class Reader extends BaseEntity{
    private String name;
    private String email;
    private List<Book> borrowedBooks;

    public Reader(String name, String email, List<Book> borrowedBooks) {
        this.name = name;
        this.email = email;
        this.borrowedBooks = borrowedBooks;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
