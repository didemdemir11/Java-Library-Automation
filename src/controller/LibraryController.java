package controller;

import entity.*;
import service.BookService;
import service.InvoiceService;
import service.LoanService;

import java.util.*;

public class LibraryController {
    private final Librarian defaultLibrarian;
    private final Reader defaultReader;
    public static void main(String[] args) {
        LibraryController libraryController = new LibraryController();
        libraryController.start();
    }
    private final BookService bookService;
    private final LoanService loanService;
    private final InvoiceService invoiceService;
    private final Scanner scanner;

    public LibraryController() {
        this.bookService = new BookService();
        this.loanService = new LoanService();
        this.invoiceService = new InvoiceService();
        this.scanner = new Scanner(System.in);
        bookService.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 15.99, "Mevcut", "2nd Edition"));
        bookService.addBook(new Book("1984", "George Orwell", "Dystopian", 12.50, "Mevcut", "1st Edition"));
        bookService.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "Classic", 10.75, "Mevcut", "3rd Edition"));
        bookService.addBook(new Book("Pride and Prejudice", "Jane Austen", "Romance", 9.99, "Mevcut", "5th Edition"));
        bookService.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 11.49, "Mevcut", "4th Edition"));
        bookService.addBook(new Book("Animal Farm", "George Orwell", "Dystopian", 8.99, "Mevcut", "2nd Edition"));
        bookService.addBook(new Book("The Fellowship of the Ring", "J.R.R. Tolkien", "Fantasy", 14.99, "Mevcut", "1st Edition"));
        bookService.addBook(new Book("Emma", "Jane Austen", "Romance", 10.50, "Mevcut", "6th Edition"));
        bookService.addBook(new Book("Brave New World", "Aldous Huxley", "Dystopian", 13.50, "Mevcut", "3rd Edition"));
        bookService.addBook(new Book("Moby-Dick", "Herman Melville", "Classic", 17.00, "Mevcut", "2nd Edition"));

        this.defaultLibrarian = new Librarian("Admin", "admin@library.com", "password123", Set.of(Responsibility.BOOK_MANAGEMENT, Responsibility.READER_MANAGEMENT, Responsibility.FINANCIAL_OPERATIONS));
        this.defaultReader = new Reader("John Doe", "johndoe@example.com", "readerpass", new ArrayList<>());
    }

    public void start() {
        while (true) {
            System.out.println("\nDidi Kütüphane Sistemine Hoş Geldiniz!");
            System.out.println("1. Librarian Girişi");
            System.out.println("2. Reader Girişi");
            System.out.println("3. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    librarianMenu();
                    break;
                case "2":
                    readerMenu();
                    break;
                case "3":
                    System.out.println("Sistemden çıkış yapılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim! Tekrar deneyin.");
            }
        }
    }

    private void librarianMenu() {
        System.out.println("\nLibrarian Girişi");
        System.out.print("E-posta girin: ");
        String email = scanner.nextLine().trim();
        System.out.print("Şifre girin: ");
        String password = scanner.nextLine().trim();
        Librarian librarian = new Librarian("Admin", email, password, Set.of(Responsibility.BOOK_MANAGEMENT, Responsibility.READER_MANAGEMENT, Responsibility.FINANCIAL_OPERATIONS));
        while (true) {
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Tüm Kitapları Listele");
            System.out.println("3. Kitapları ID’ye göre Listele");
            System.out.println("4. Kitapları Yazara göre Listele");
            System.out.println("5. Kitapları Kategoriye göre Listele");
            System.out.println("6. Kitap Sil");
            System.out.println("7. Kitap Bilgilerini Güncelle");
            System.out.println("8. Kitapları id, kategori veya yazara göre arat");
            System.out.println("9. Kitap Ödünç Ver");
            System.out.println("10. Kitap İade Al");
            System.out.println("11. Fatura Kes");
            System.out.println("12. Para İadesi Yap");
            System.out.println("13. Geri Dön");
            System.out.print("Seçiminizi yapın: ");
            String choice = scanner.nextLine().trim();


            switch (choice) {
                case "1":
                    bookService.addBookFromInput(scanner);
                    break;
                case "2":
                    bookService.listAllBooks();
                    break;
                case "3":
                    bookService.getBookByIdFromInput(scanner);
                    break;
                case "4":
                    bookService.getBooksByAuthorFromInput(scanner);
                    break;
                case "5":
                    bookService.getBooksByCategoryFromInput(scanner);
                    break;
                case "6":
                    bookService.removeBookFromInput(scanner, librarian);
                    break;
                case "7":
                    bookService.updateBookDetailsFromInput(scanner, librarian);
                    break;
                case "8":
                    System.out.println("Arama türünü seçin:");
                    System.out.println("1 - ID ile ara");
                    System.out.println("2 - Yazar adı ile ara");
                    System.out.println("3 - Kategori ile ara");
                    System.out.print("Seçiminizi yapın: ");
                    String searchType = scanner.nextLine().trim();

                    switch (searchType) {
                        case "1":
                            System.out.print("Kitap ID girin: ");
                            long bookId = scanner.nextLong();
                            scanner.nextLine(); // Buffer temizleme
                            bookService.getBookById(bookId).ifPresentOrElse(
                                    System.out::println,
                                    () -> System.out.println("Hata: Kitap bulunamadı.")
                            );
                            break;

                        case "2":
                            System.out.print("Yazar adını girin: ");
                            String author = scanner.nextLine().trim();
                            bookService.getBooksByAuthor(author).ifPresentOrElse(
                                    books -> books.forEach(System.out::println),
                                    () -> System.out.println("Hata: Bu yazara ait kitap bulunamadı.")
                            );
                            break;

                        case "3":
                            System.out.print("Kategori adını girin: ");
                            String category = scanner.nextLine().trim();
                            bookService.getBooksByCategory(category).ifPresentOrElse(
                                    books -> books.forEach(System.out::println),
                                    () -> System.out.println("Hata: Bu kategoriye ait kitap bulunamadı.")
                            );
                            break;

                        default:
                            System.out.println("Geçersiz seçim! Tekrar deneyin.");
                    }
                    break;
                case "9":
                    System.out.println("Ödünç vermek istediğiniz kitabın ID'sini girin: ");
                    long loanBookId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Ödünç alacak okuyucunun adını girin: ");
                    String readerName = scanner.nextLine().trim();
                    System.out.print("Ödünç alacak okuyucunun e-posta adresini girin: ");
                    String readerEmail = scanner.nextLine().trim();
                    Reader reader = loanService.getReaderByEmail(readerEmail)
                            .orElseGet(() -> new Reader("Unknown", readerEmail, "defaultPassword", new ArrayList<>()));
                    loanService.borrowBook(scanner, reader, bookService.getBookById(loanBookId).orElse(null), invoiceService);
                    break;
                case "10":
                    System.out.println("İade alınacak kitabın ID'sini girin: ");
                    long returnBookId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("İade eden okuyucunun adını girin: ");
                    String returnReaderName = scanner.nextLine().trim();
                    System.out.print("İade eden okuyucunun e-posta adresini girin: ");
                    String returnReaderEmail = scanner.nextLine().trim();
                    Reader returnReader = loanService.getReaderByEmail(returnReaderEmail)
                            .orElseGet(() -> new Reader("Unknown", returnReaderEmail, "defaultPassword", new ArrayList<>()));
                    loanService.returnBook(scanner, returnReader, bookService.getBookById(returnBookId).orElse(null), invoiceService);
                    break;
                case "11":
                    System.out.println("Fatura kesmek için ödünç alınan kitabın ID'sini girin: ");
                    long invoiceBookId = scanner.nextLong();
                    scanner.nextLine();

                    Optional<Loan> loanForInvoice = loanService.getLoanByBookId(invoiceBookId);
                    if (loanForInvoice.isPresent()) {
                        invoiceService.generateInvoice(scanner, loanForInvoice.get());
                    } else {
                        System.out.println("Hata: Bu kitap için geçerli bir ödünç kaydı bulunamadı.");
                    }
                    break;
                case "12":
                    System.out.println("Para iadesi yapmak için ödünç alınan kitabın ID'sini girin: ");
                    long refundBookId = scanner.nextLong();
                    scanner.nextLine();

                    Optional<Loan> loanForRefund = loanService.getLoanByBookId(refundBookId);
                    if (loanForRefund.isPresent()) {
                        invoiceService.refundInvoice(scanner, loanForRefund.get());
                    } else {
                        System.out.println("Hata: Bu kitap için geçerli bir ödünç kaydı bulunamadı.");
                    }
                    break;
                case "13":
                    return;
                default:
                    System.out.println("Geçersiz seçim! Tekrar deneyin.");
            }
        }
    }

    private void readerMenu() {
        System.out.println("\nReader Girişi");
        System.out.print("Reader adı girin: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Reader e-posta girin: ");
        String email = scanner.nextLine().trim();
        System.out.print("Şifre girin: ");
        String password = scanner.nextLine().trim();
        Reader reader = new Reader(fullName, email, password, new ArrayList<>());
        while (true) {
            System.out.println("1. Kitap Ara (ID, Başlık, Kategori ya da Yazara göre)");
            System.out.println("2. Kitap Ödünç Al");
            System.out.println("3. Kitap İade Et");
            System.out.println("4. İade Edilecek Kitapları Listele");
            System.out.println("5. Geri Dön");
            System.out.println("6. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Kitap arama kriterinizi seçin: ");
                    System.out.println("1 - Kitap ID ile ara");
                    System.out.println("2 - Kitap Başlık ile ara");
                    System.out.println("3 - Kitap Kategori ile ara");
                    System.out.println("4 - Kitap Yazar ile ara");
                    System.out.print("Seçiminizi yapın: ");

                    String searchChoice = scanner.nextLine().trim();

                    switch (searchChoice) {
                        case "1":
                            bookService.getBookByIdFromInput(scanner);
                            break;
                        case "2":
                            System.out.print("Kitap başlığını girin: ");
                            String title = scanner.nextLine().trim();
                            bookService.getBookByTitle(title).ifPresentOrElse(
                                    System.out::println,
                                    () -> System.out.println("Hata: Kitap bulunamadı.")
                            );
                            break;
                        case "3":
                            bookService.getBooksByCategoryFromInput(scanner);
                            break;
                        case "4":
                            bookService.getBooksByAuthorFromInput(scanner);
                            break;
                        default:
                            System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
                    }
                    break;
                case "2":
                    System.out.println("Ödünç almak istediğiniz kitabın ID'sini girin: ");
                    long bookId = scanner.nextLong();
                    scanner.nextLine();
                    loanService.borrowBook(scanner, reader, bookService.getBookById(bookId).orElse(null), invoiceService);
                    break;
                case "3":
                    System.out.println("İade etmek istediğiniz kitabın ID'sini girin: ");
                    long returnBookId = scanner.nextLong();
                    scanner.nextLine();
                    loanService.returnBook(scanner, reader, bookService.getBookById(returnBookId).orElse(null), invoiceService);
                    break;
                case "4":
                    System.out.println("İade edilecek kitaplar: ");
                    loanService.listBorrowedBooks(reader);
                    break;
                case "5":
                    return;
                case "6":
                    System.out.println("Sistemden çıkış yapılıyor...");
                    System.exit(0);
                default:
                    System.out.println("Geçersiz seçim! Tekrar deneyin.");
            }
        }
    }
}


