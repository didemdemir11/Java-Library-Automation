package service;

import entity.Reader;

import java.time.LocalDate;

public class LoanRecord {
    private Reader reader;
    private LocalDate borrowDate;

    public LoanRecord(Reader reader, LocalDate borrowDate) {
        this.reader = reader;
        this.borrowDate = borrowDate;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }
}
