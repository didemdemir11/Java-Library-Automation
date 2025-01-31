package entity;

import java.util.List;

public class Library extends BaseEntity{
    private String name;
    private List<Book> books;
    private List<Reader> readers;
    private List<Librarian> librarians;

    public Library(String name, List<Book> books, List<Reader> readers, List<Librarian> librarians) {
        this.name = name;
        this.books = books;
        this.readers = readers;
        this.librarians = librarians;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }
}
