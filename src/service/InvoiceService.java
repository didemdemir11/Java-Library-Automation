package service;


import entity.Invoice;
import entity.Loan;

import java.time.LocalDate;
import java.util.Scanner;

public class InvoiceService {
    public void generateInvoice(Scanner scanner, Loan loan) {
        LocalDate issueDate = LocalDate.now();
        double amount = Math.min(loan.getReturnDate().toEpochDay() - loan.getBorrowDate().toEpochDay(), 15) * 5.0;
        Invoice invoice = new Invoice(loan, amount, issueDate, false);
        loan.setInvoice(invoice);
        System.out.println("Fatura kesildi: " + amount + " TL, Tarih: " + issueDate);
    }

    public void refundInvoice(Scanner scanner, Loan loan) {
        if (loan.getInvoice() == null || loan.getInvoice().isPaid()) {
            System.out.println("Hata: Fatura zaten ödenmiş veya bulunamadı.");
            return;
        }
        System.out.println("Fatura tutarı: " + loan.getInvoice().getAmount() + " TL");
        System.out.print("Ödeme iade edilsin mi? (E/H): ");
        String choice = scanner.nextLine().trim().toUpperCase();
        if (choice.equals("E")) {
            loan.getInvoice().markAsPaid();
            System.out.println("Ödeme iade edildi: " + loan.getInvoice().getAmount() + " TL");
        } else {
            System.out.println("İade işlemi iptal edildi.");
        }
    }
}

