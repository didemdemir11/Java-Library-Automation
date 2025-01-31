package service;

import entity.Book;

import java.util.*;

public class BookService {
    private Map<Long, Book> books;

    public BookService() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
        System.out.println(book.getTitle() + " adlı kitap eklendi.");
    }

    public Optional<Book> getBookById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Optional<Book> getBookByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    public void updateBookDetails(long id, String newTitle, String newAuthor, String newCategory, double newPrice, String newStatus) {
        books.computeIfPresent(id, (key, book) -> {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setCategory(newCategory);
            book.setPrice(newPrice);
            book.setStatus(newStatus);
            System.out.println("Kitap bilgileri güncellendi: " + newTitle);
            return book;
        });
    }

    public void removeBook(long id) {
        books.remove(id);
        System.out.println("Kitap başarıyla silindi.");
    }

    public void listAllBooks() {
        books.values().forEach(book -> System.out.println(book.getId() + " - " + book.getTitle() + " - " + book.getStatus()));
    }
}

