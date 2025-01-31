package ui;

import entity.Book;
import entity.Librarian;
import entity.Reader;
import entity.Responsibility;
import service.BookService;
import service.InvoiceService;
import service.LoanService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        BookService bookService = new BookService();
        LoanService loanService = new LoanService();
        InvoiceService invoiceService = new InvoiceService();
        Map<String, Reader> readers = new HashMap<>();
        Map<String, Librarian> librarians = new HashMap<>();
        Librarian librarian = new Librarian("admin", Stream.of(Responsibility.BOOK_MANAGEMENT, Responsibility.READER_MANAGEMENT, Responsibility.FINANCIAL_OPERATIONS).collect(Collectors.toSet()));
        librarians.put(librarian.getName(), librarian);

        while (true){
            System.out.println("Didi Kütüphane Sistemi");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Kitapları listele");
            System.out.println("3. Kitap Ödünç Al");
            System.out.println("4. Kitap İade Et");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection){
                case 1:
                    System.out.println("Kitap adı/Title Name:");
                    String title = scanner.nextLine();
                    System.out.println("Yazar/Author: ");
                    String author = scanner.nextLine();
                    System.out.println("Kategori/Category: ");
                    String category = scanner.nextLine();
                    System.out.println("Fiyat: ");
                    double price = scanner.nextDouble();
                    System.out.println("Status: ");
                    scanner.nextLine();
                    scanner.nextLine();
                    System.out.print("Baskı (Edition): ");
                    String edition = scanner.nextLine();
                    System.out.print("Satın Alma Tarihi (YYYY-MM-DD): ");
                    String purchaseDateInput = scanner.nextLine();
                    LocalDate purchaseDate = LocalDate.parse(purchaseDateInput);
                    String status = "Mevcut";
                    Book book = new Book(title, author, category, price, status, edition, purchaseDate);
                    bookService.addBook(book);
                    break;
                case 2:
                    bookService.listAllBooks();
                    break;
                case 3:
                    System.out.print("Ödünç almak istediğiniz kitabın ID'sini girin: ");
                    long bookId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Okuyucu Adı: ");
                    String readerName = scanner.nextLine();
                    Reader reader = readers.computeIfAbsent(readerName, Reader::new);
                    bookService.getBookById(bookId).ifPresent(bookToBorrow -> loanService.borrowBook(bookToBorrow, reader));
                    break;
                case 4:
                    System.out.print("İade etmek istediğiniz kitabın ID'sini girin: ");
                    long returnBookId = scanner.nextLong();
                    scanner.nextLine();
                    bookService.getBookById(returnBookId).ifPresent(bookToReturn -> {
                        loanService.getReaderByBookId(returnBookId).ifPresent(readerToReturn ->
                                loanService.returnBook(bookToReturn, readerToReturn, invoiceService));
                    });
                    break;
                case 5:
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Geçersiz seçim, tekrar deneyin.");
            }
            }
        }
    }

