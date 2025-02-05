package service;


import entity.Invoice;
import entity.Loan;
import repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void generateInvoice(Scanner scanner, Loan loan) {
        LocalDate issueDate = LocalDate.now();
        double amount = Math.min(loan.getReturnDate().toEpochDay() - loan.getBorrowDate().toEpochDay(), 15) * 5.0;

        Invoice invoice = new Invoice(loan, amount, issueDate, false);
        loan.setInvoice(invoice);
        invoiceRepository.saveInvoice(invoice);

        System.out.println("Fatura kesildi: " + amount + " TL, Tarih: " + issueDate);
    }

    public void refundInvoice(Scanner scanner, Loan loan) {
        if (loan.getInvoice() == null || loan.getInvoice().isPaid()) {
            System.out.println("Hata: Fatura zaten ödenmiş veya bulunamadı.");
            return;
        }

        System.out.println("Fatura tutarı: " + loan.getInvoice().getAmount() + " TL");

        String choice;
        while (true) { // Kullanıcı geçerli bir giriş yapana kadar sor
            System.out.print("Ödeme iade edilsin mi? (E/H): ");
            choice = scanner.nextLine().trim().toUpperCase();
            if (choice.equals("E") || choice.equals("H")) break;
            System.out.println("Hata: Lütfen sadece 'E' veya 'H' girin.");
        }

        if (choice.equals("E")) {
            loan.getInvoice().markAsPaid();
            System.out.println("Ödeme iade edildi: " + loan.getInvoice().getAmount() + " TL");
        } else {
            System.out.println("İade işlemi iptal edildi.");
        }
    }
    public Optional<Invoice> findInvoiceByLoan(Loan loan) {
        return invoiceRepository.findInvoiceByLoan(loan);
    }
    public void listAllInvoices() {
        invoiceRepository.findAllInvoices().forEach(System.out::println);
    }
    public void updateInvoice(Invoice invoice) {
        invoiceRepository.updateInvoice(invoice);
    }
}

