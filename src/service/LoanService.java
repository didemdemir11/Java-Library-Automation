package service;

import entity.Book;
import entity.Invoice;
import entity.Loan;
import entity.Reader;
import exception.BookAlreadyLoanedException;
import exception.InvalidInputException;
import exception.MaxLoanLimitExceededException;
import exception.ReaderNotFoundException;
import repository.LoanRepository;


import java.time.LocalDate;

import java.util.*;

public class LoanService {
    private final LoanRepository loanRepository;
    private final InvoiceService invoiceService;

    public LoanService(LoanRepository loanRepository, InvoiceService invoiceService) {
        this.loanRepository = loanRepository;
        this.invoiceService = invoiceService;
    }

    public void borrowBook(Scanner scanner, Reader reader, Book book) {
        if (reader.getBorrowedBooks().size() >= 5) {
            throw new MaxLoanLimitExceededException("Hata: Kullanıcı en fazla 5 kitap ödünç alabilir.");
        }

        if (!book.getStatus().equalsIgnoreCase("Mevcut")) {
            throw new BookAlreadyLoanedException("Hata: Kitap şu anda başka bir kullanıcı tarafından ödünç alınmış.");
        }

        System.out.print("Kaç gün ödünç almak istiyorsunuz? (Maksimum 15 gün): ");
        int days;
        try {
            days = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            throw new InvalidInputException("Hata: Geçerli bir gün sayısı girin.");
        }

        if (days > 15) {
            System.out.println("Hata: Maksimum ödünç alma süresi 15 gündür.");
            return;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(days);
        Loan loan = new Loan(reader, book, borrowDate, returnDate, false, null);

        // Fatura oluştur
        Invoice invoice = new Invoice(loan, days * 5.0, borrowDate, false);
        loan.setInvoice(invoice);

        // Loan'ı repo'ya ekle
        loanRepository.saveLoan(loan);
        book.setStatus("Ödünç Alındı");
        reader.getBorrowedBooks().add(book);

        // LoanRecord ekle
        LoanRecord loanRecord = new LoanRecord(reader, book, borrowDate, returnDate);
        loanRepository.saveLoanRecord(loanRecord);

        // Faturayı işleme al
        invoiceService.generateInvoice(scanner, loan);

        System.out.println("Kitap ödünç alındı: " + book.getTitle() + " - Teslim tarihi: " + returnDate);
    }

    public void returnBook(Scanner scanner, Reader reader, Book book) {
        Optional<Loan> loanOpt = loanRepository.findLoanByBookAndReader(book, reader);

        if (loanOpt.isEmpty()) {
            System.out.println("Hata: Bu kitap bu kullanıcı tarafından ödünç alınmamış.");
            return;
        }

        Loan loan = loanOpt.get();
        LocalDate today = LocalDate.now();
        long overdueDays = today.isAfter(loan.getReturnDate()) ? today.toEpochDay() - loan.getReturnDate().toEpochDay() : 0;

        if (overdueDays > 0) {
            double penalty = overdueDays * 10;
            System.out.println("Gecikme süresi: " + overdueDays + " gün. Cezanız: " + penalty + " TL");

            // Fatura güncelleniyor
            loan.getInvoice().setAmount(loan.getInvoice().getAmount() + penalty);
            loan.getInvoice().setPaid(false); // Fatura tekrar ödenmeyi bekleyecek
            invoiceService.updateInvoice(loan.getInvoice());
        } else {
            invoiceService.refundInvoice(scanner, loan);
        }

        book.setStatus("Mevcut");
        reader.getBorrowedBooks().remove(book);
        loanRepository.deleteLoan(loan);
        System.out.println("Kitap başarıyla iade edildi: " + book.getTitle());
    }

    public void listLoanRecords() {
        loanRepository.findAllLoanRecords().forEach(System.out::println);
    }




    public void listBorrowedBooks(Reader reader) {
        List<Loan> borrowedLoans = loanRepository.findLoansByReader(reader);
        if (borrowedLoans.isEmpty()) {
            System.out.println("Üzerinizde iade edilmesi gereken kitap bulunmamaktadır.");
        } else {
            System.out.println("İade edilmesi gereken kitaplar:");
            borrowedLoans.forEach(loan -> {
                System.out.println("ID: " + loan.getBook().getId() +
                        " - " + loan.getBook().getTitle() +
                        " (Teslim tarihi: " + loan.getReturnDate() + ")");
            });
        }
    }

    public Optional<Loan> getLoanByBookId(long bookId) {
        return loanRepository.findLoanByBookId(bookId);
    }
    public Optional<Reader> getReaderByEmail(String email) {
        return loanRepository.findReaderByEmail(email);
    }
    public void addReader(Reader reader) {
        if (loanRepository.findReaderByEmail(reader.getEmail()).isEmpty()) {
            loanRepository.saveReader(reader);
            System.out.println("Yeni okuyucu kaydedildi: " + reader.getFullName());
        } else {
            System.out.println("Okuyucu zaten kayıtlı: " + reader.getFullName());
        }
    }
}