package entity;

import java.time.LocalDate;

public class Loan extends BaseEntity{
    private Reader reader;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returnStatus;

    public Loan(Reader reader, Book book, LocalDate borrowDate, LocalDate returnDate,boolean returnStatus) {
        this.reader = reader;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returnStatus = returnStatus;
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

    public boolean isReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(boolean returnStatus) {
        this.returnStatus = returnStatus;
    }
}
