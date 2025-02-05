package entity;

import java.util.ArrayList;
import java.util.List;

public class Author extends BaseEntity{
    private String authorName;
    private List<Book> books;

    public Author(String authorName) {
        super();
        this.authorName = authorName;
        this.books = new ArrayList<>();
    }

    public String getAuthorName() {
        return authorName;
    }

    public List<Book> getBooks() {
        return books;
    }
}
