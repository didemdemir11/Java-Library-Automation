package service;

import entity.Book;
import entity.Reader;

import java.util.HashMap;
import java.util.Map;

public class InvoiceService {
    private Map<Long, Double> invoices;

    public InvoiceService() {
        this.invoices = new HashMap<>();
    }

    public void generateInvoice(Book book, Reader reader, double amount) {
        invoices.put(book.getId(), amount);
        System.out.println(reader.getName() + " için " + book.getTitle() + " kitabı için fatura oluşturuldu. Tutar: " + amount + " TL");
    }

    public void refundInvoice(Book book, Reader reader) {
        if (invoices.containsKey(book.getId())) {
            double amount = invoices.remove(book.getId());
            System.out.println(reader.getName() + " için " + book.getTitle()+ " kitabının iadesi yapıldı. Geri ödenen tutar: " + amount + " TL");
        } else {
            System.out.println("Bu kitap için fatura bulunamadı.");
        }
    }
}
