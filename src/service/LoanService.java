package service;

import entity.Book;
import entity.Reader;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoanService {
    private Map<Long, LoanRecord> borrowedBooks;

    public LoanService() {
        this.borrowedBooks = new HashMap<>();
    }
    public void borrowBook(Book book,Reader reader){
        if (borrowedBooks.containsKey(book.getId())){
            System.out.println("Bu kitap ödünç alınmış: "+book.getTitle());
        } else if (reader.getBorrowedBooks().size() >= 5) {
            System.out.println("5 kitaptan fazla kitap ödünç alamazsınız:  "+reader.getName());
        } else {
            borrowedBooks.put(book.getId(), new LoanRecord(reader, LocalDate.now()));
            reader.getBorrowedBooks().add(book);
            System.out.println(reader.getName()+" adlı okuyucu "+book.getTitle()+" kitabını ödünç aldı.");
        }
    }
    public void returnBook(Book book, Reader reader,InvoiceService invoiceService) {
        if (borrowedBooks.containsKey(book.getId()) && borrowedBooks.get(book.getId()).getReader().equals(reader)) {
            LoanRecord loanRecord = borrowedBooks.remove(book.getId());
            reader.getBorrowedBooks().remove(book);

            long daysBorrowed = ChronoUnit.DAYS.between(loanRecord.getBorrowDate(), LocalDate.now());
            System.out.println(reader.getName() + " adlı okuyucu " + book.getTitle() + " kitabını iade etti. Ödünç süresi: " + daysBorrowed + " gün");
            if (daysBorrowed > 15) {
                double lateFee = (daysBorrowed - 15) * 10;
                invoiceService.generateInvoice(book, reader, lateFee);
                System.out.println("Gecikme ücreti: " + lateFee + " TL");
            }
        } else {
            System.out.println("Bu kitap ödünç alınmamış veya farklı bir okuyucuya ait.");
        }
    }
    public Optional<Reader> getReaderByBookId(long bookId) {
        return Optional.ofNullable(borrowedBooks.get(bookId)).map(LoanRecord::getReader);
    }
}
