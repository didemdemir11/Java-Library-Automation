package service;

import entity.Book;
import entity.Invoice;
import entity.Loan;
import entity.Reader;


import java.time.LocalDate;

import java.util.*;

public class LoanService {
    private final List<Loan> loans;
    private final List<LoanRecord> loanRecords;
    private final List<Reader> readers;
    public LoanService() {
        this.loans = new ArrayList<>();
        this.loanRecords = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void borrowBook(Scanner scanner, Reader reader, Book book, InvoiceService invoiceService) {
        if (reader.getBorrowedBooks().size() >= 5) {
            System.out.println("Hata: Kullanıcı en fazla 5 kitap ödünç alabilir.");
            return;
        }

        if (!book.getStatus().equalsIgnoreCase("Mevcut")) {
            System.out.println("Hata: Kitap şu anda başka bir kullanıcı tarafından ödünç alınmış.");
            return;
        }

        System.out.print("Kaç gün ödünç almak istiyorsunuz? (Maksimum 15 gün): ");
        int days = scanner.nextInt();
        scanner.nextLine();
        if (days > 15) {
            System.out.println("Hata: Maksimum ödünç alma süresi 15 gündür.");
            return;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(days);
        Loan loan = new Loan(reader, book, borrowDate, returnDate,false,null);
        loan.setInvoice(new Invoice(loan, Math.min(days, 15) * 5.0, LocalDate.now(), false));
        loans.add(loan);
        book.setStatus("Ödünç Alındı");
        reader.getBorrowedBooks().add(book);
        loanRecords.add(new LoanRecord(reader, book, borrowDate, returnDate));

        invoiceService.generateInvoice(scanner, loan);

        System.out.println("Kitap ödünç alındı: " + book.getTitle() + " - Teslim tarihi: " + returnDate);
    }

    public void returnBook(Scanner scanner, Reader reader, Book book, InvoiceService invoiceService) {
        Optional<Loan> loanOpt = loans.stream()
                .filter(loan -> loan.getBook().equals(book) && loan.getReader().equals(reader))
                .findFirst();

        if (loanOpt.isEmpty()) {
            System.out.println("Hata: Bu kitap bu kullanıcı tarafından ödünç alınmamış.");
            return;
        }

        Loan loan = loanOpt.get();
        LocalDate today = LocalDate.now();
        long overdueDays = today.isAfter(loan.getReturnDate()) ? today.toEpochDay() - loan.getReturnDate().toEpochDay() : 0;
        double penalty = overdueDays * 10;

        if (overdueDays > 0) {
            System.out.println("Gecikme süresi: " + overdueDays + " gün. Cezanız: " + penalty + " TL");
        } else {
            invoiceService.refundInvoice(scanner, loan);
        }

        book.setStatus("Mevcut");
        reader.getBorrowedBooks().remove(book);
        loans.remove(loan);
        System.out.println("Kitap başarıyla iade edildi: " + book.getTitle());
    }

    public void listLoanRecords() {
        if (loanRecords.isEmpty()) {
            System.out.println("Henüz ödünç alınmış bir kitap yok.");
        } else {
            loanRecords.forEach(System.out::println);
        }
    }
    public Optional<Loan> getLoanByBookId(long bookId) {
        return loans.stream()
                .filter(loan -> loan.getBook().getId() == bookId)
                .findFirst();
    }
    public Optional<Reader> getReaderByEmail(String email) {

        return readers.stream()
                .filter(reader -> reader.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
    public void addReader(Reader reader) {
        readers.add(reader);
    }
    public void listBorrowedBooks(Reader reader) {
        List<Book> borrowedBooks = reader.getBorrowedBooks();
        if (borrowedBooks.isEmpty()) {
            System.out.println("Üzerinizde iade edilmesi gereken kitap bulunmamaktadır.");
        } else {
            System.out.println("İade edilmesi gereken kitaplar:");
            borrowedBooks.forEach(book ->
                    System.out.println("ID: " + book.getId() + " - " + book.getTitle() + " (Teslim tarihi: " + getLoanByBookId(book.getId()).map(Loan::getReturnDate).orElse(null) + ")")
            );
        }
    }
}
