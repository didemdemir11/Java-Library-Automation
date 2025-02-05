package service;

import entity.Book;
import entity.Reader;

import java.time.LocalDate;

public class LoanRecord {
    private final Reader reader;
    private final Book book;
    private final LocalDate borrowDate;
    private final LocalDate returnDate;

    public LoanRecord(Reader reader, Book book, LocalDate borrowDate, LocalDate returnDate) {
        this.reader = reader;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    @Override
    public String toString() {
        return "Ödünç Alan: " + reader.getFullName() +
                ", Kitap: " + book.getTitle() +
                ", Ödünç Alma Tarihi: " + borrowDate +
                ", İade Tarihi: " + returnDate;
    }
}