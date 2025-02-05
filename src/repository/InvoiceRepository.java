package repository;

import entity.Invoice;
import entity.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceRepository {
    private final List<Invoice> invoices = new ArrayList<>();

    public void saveInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    public void updateInvoice(Invoice invoice) {
        invoices.replaceAll(existingInvoice ->
                existingInvoice.getLoan().equals(invoice.getLoan()) ? invoice : existingInvoice
        );
    }

    public Optional<Invoice> findInvoiceByLoan(Loan loan) {
        return invoices.stream()
                .filter(invoice -> invoice.getLoan().equals(loan))
                .findFirst();
    }

    public List<Invoice> findAllInvoices() {
        return new ArrayList<>(invoices);
    }
}