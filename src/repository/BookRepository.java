package repository;

import entity.Book;
import exception.BookNotFoundException;

import java.util.*;

public class BookRepository {private final Map<Long, Book> books = new HashMap<>();

    public void save(Book book) {
        books.put(book.getId(), book);
    }

    public Optional<Book> findById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> findByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    public List<Book> findByCategory(String category) {
        return books.values().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }
    public Optional<Book> findByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }
    public void deleteById(long id) {
        books.remove(id);
    }
    public void update(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book);
        } else {
            throw new BookNotFoundException("Kitap bulunamadı.");
        }
    }
}