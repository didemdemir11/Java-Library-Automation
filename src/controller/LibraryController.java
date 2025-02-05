package controller;

import entity.*;
import exception.BookNotFoundException;
import repository.BookRepository;
import repository.InvoiceRepository;
import repository.LoanRepository;
import service.BookService;
import service.InvoiceService;
import service.LoanService;

import java.util.*;
public class LibraryController {
    private final Librarian defaultLibrarian;
    private final Reader defaultReader;
    private final BookService bookService;
    private final LoanService loanService;
    private final InvoiceService invoiceService;
    private final Scanner scanner;

    public static void main(String[] args) {
        LibraryController libraryController = new LibraryController();
        libraryController.start();
    }

    public LibraryController() {
        BookRepository bookRepository = new BookRepository();
        InvoiceRepository invoiceRepository = new InvoiceRepository();
        LoanRepository loanRepository = new LoanRepository();


        this.bookService = new BookService(bookRepository);
        this.invoiceService = new InvoiceService(invoiceRepository);
        this.loanService = new LoanService(loanRepository,invoiceService);
        this.scanner = new Scanner(System.in);

        // Örnek kitaplar ekleniyor
        initializeSampleBooks();

        // Varsayılan kullanıcılar ekleniyor
        this.defaultLibrarian = new Librarian("Admin", "admin@library.com", "password123",
                Set.of(Responsibility.BOOK_MANAGEMENT, Responsibility.READER_MANAGEMENT, Responsibility.FINANCIAL_OPERATIONS));
        this.defaultReader = new Reader("John Doe", "johndoe@example.com", "readerpass", new ArrayList<>());
        loanRepository.saveReader(defaultReader);
    }

    private void initializeSampleBooks() {
        bookService.save(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", 15.99, "Mevcut", "2nd Edition"));
        bookService.save(new Book("1984", "George Orwell", "Dystopian", 12.50, "Mevcut", "1st Edition"));
        bookService.save(new Book("To Kill a Mockingbird", "Harper Lee", "Classic", 10.75, "Mevcut", "3rd Edition"));
        bookService.save(new Book("Pride and Prejudice", "Jane Austen", "Romance", 9.99, "Mevcut", "5th Edition"));
        bookService.save(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 11.49, "Mevcut", "4th Edition"));
        bookService.save(new Book("Animal Farm", "George Orwell", "Dystopian", 8.99, "Mevcut", "2nd Edition"));
        bookService.save(new Book("The Fellowship of the Ring", "J.R.R. Tolkien", "Fantasy", 14.99, "Mevcut", "1st Edition"));
        bookService.save(new Book("Emma", "Jane Austen", "Romance", 10.50, "Mevcut", "6th Edition"));
        bookService.save(new Book("Brave New World", "Aldous Huxley", "Dystopian", 13.50, "Mevcut", "3rd Edition"));
        bookService.save(new Book("Moby-Dick", "Herman Melville", "Classic", 17.00, "Mevcut", "2nd Edition"));
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
                case "1" -> librarianMenu();
                case "2" -> readerMenu();
                case "3" -> {
                    System.out.println("Sistemden çıkış yapılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim! Tekrar deneyin.");
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
        if (!email.equals(defaultLibrarian.getEmail()) || !password.equals(defaultLibrarian.getPassword())) {
            System.out.println("Hata: Geçersiz e-posta veya şifre!");
            return;  // Yanlış giriş yapıldığında menüye dön
        }

        System.out.println("Giriş başarılı! Hoş geldiniz, " + defaultLibrarian.getFullName());
        while (true) {
            System.out.println("""
                    1. Kitap Ekle
                    2. Tüm Kitapları Listele
                    3. Kitap Ara (ID, Başlık, Kategori, Yazar)
                    4. Kitap Sil
                    5. Kitap Bilgilerini Güncelle
                    6. Kitap Ödünç Ver
                    7. Kitap İade Al
                    8. Fatura Kes
                    9. Para İadesi Yap
                    10. Geri Dön
                    """);
            System.out.print("Seçiminizi yapın: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> bookService.addBookFromInput(scanner);
                case "2" -> bookService.listAllBooks();
                case "3" -> searchBooks();
                case "4" -> bookService.removeBookFromInput(scanner, librarian);
                case "5" -> bookService.updateBookDetailsFromInput(scanner, librarian);
                case "6" -> borrowBookProcess();
                case "7" -> returnBookProcess();
                case "8" -> generateInvoiceProcess();
                case "9" -> refundInvoiceProcess();
                case "10" -> { return; }
                default -> System.out.println("Geçersiz seçim! Tekrar deneyin.");
            }
        }
    }

    private void readerMenu() {
        System.out.println("\nReader Girişi");
        System.out.print("Reader e-posta girin: ");
        String email = scanner.nextLine().trim();

        Optional<Reader> existingReader = loanService.getReaderByEmail(email);

        Reader reader;

        if (existingReader.isPresent()) {
            // Kullanıcı zaten kayıtlıysa, şifre doğrulama yap
            System.out.print("Şifre girin: ");
            String password = scanner.nextLine().trim();

            if (!existingReader.get().getPassword().equals(password)) {
                System.out.println("Hatalı şifre! Giriş başarısız.");
                return;
            }

            reader = existingReader.get();
            System.out.println("Giriş başarılı! Hoş geldiniz, " + reader.getFullName());

        } else {
            // Kullanıcı kayıtlı değilse, yeni kayıt oluştur
            System.out.println("Bu e-posta ile kayıtlı bir okuyucu bulunamadı. Yeni bir hesap oluşturuluyor...");
            System.out.print("Tam adınızı girin: ");
            String fullName = scanner.nextLine().trim();
            System.out.print("Şifre belirleyin: ");
            String password = scanner.nextLine().trim();

            reader = new Reader(fullName, email, password, new ArrayList<>());
            loanService.addReader(reader);
            System.out.println("Yeni okuyucu kaydedildi: " + fullName);

        }

        // Yeni okuyucuysa şifre sorma, zaten giriş yaptı
        while (true) {
            System.out.println("""
                1. Kitap Ara (ID, Başlık, Kategori ya da Yazara göre)
                2. Kitap Ödünç Al
                3. Kitap İade Et
                4. İade Edilecek Kitapları Listele
                5. Geri Dön
                6. Çıkış
                """);
            System.out.print("Seçiminizi yapın: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> searchBooks();
                case "2" -> borrowBookProcess();
                case "3" -> returnBookProcess();
                case "4" -> loanService.listBorrowedBooks(reader);
                case "5" -> { return; }
                case "6" -> {
                    System.out.println("Sistemden çıkış yapılıyor...");
                    System.exit(0);
                }
                default -> System.out.println("Geçersiz seçim! Tekrar deneyin.");
            }
        }
    }

    private void searchBooks() {
        System.out.println("""
                Kitap arama kriterinizi seçin:
                1 - Kitap ID ile ara
                2 - Kitap Başlık ile ara
                3 - Kitap Kategori ile ara
                4 - Kitap Yazar ile ara
                """);
        System.out.print("Seçiminizi yapın: ");
        String searchChoice = scanner.nextLine().trim();

        switch (searchChoice) {
            case "1" -> bookService.getBookByIdFromInput(scanner);
            case "2" -> {
                System.out.print("Kitap başlığını girin: ");
                String title = scanner.nextLine().trim();
                bookService.getBookByTitle(title).ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Hata: Kitap bulunamadı.")
                );
            }
            case "3" -> bookService.getBooksByCategoryFromInput(scanner);
            case "4" -> bookService.getBooksByAuthorFromInput(scanner);
            default -> System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
        }
    }
    private void borrowBookProcess() {
        System.out.print("Ödünç alınacak kitabın ID'sini girin: ");
        long bookId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Okuyucu e-posta adresini girin: ");
        String email = scanner.nextLine().trim();

        Reader reader = loanService.getReaderByEmail(email)
                .orElseGet(() -> new Reader("Unknown", email, "defaultPassword", new ArrayList<>()));

        Book book = bookService.getBookById(bookId).orElseThrow(() ->
                new BookNotFoundException("Hata: Kitap bulunamadı!"));


        loanService.borrowBook(scanner, reader, book);
    }

    private void returnBookProcess() {
        System.out.print("İade edilecek kitabın ID'sini girin: ");
        long bookId = scanner.nextLong();
        scanner.nextLine();

        Book book = bookService.getBookById(bookId).orElse(null);
        if (book == null) {
            System.out.println("Hata: Kitap bulunamadı!");
            return;
        }

        System.out.print("İade eden okuyucunun e-posta adresini girin: ");
        String email = scanner.nextLine().trim();

        Reader reader = loanService.getReaderByEmail(email)
                .orElseGet(() -> new Reader("Unknown", email, "defaultPassword", new ArrayList<>()));

        loanService.returnBook(scanner, reader, book);
    }

    private void generateInvoiceProcess() {
        System.out.print("Fatura kesilecek kitabın ID'sini girin: ");
        long bookId = scanner.nextLong();
        scanner.nextLine();
        loanService.getLoanByBookId(bookId).ifPresentOrElse(
                loan -> invoiceService.generateInvoice(scanner, loan),
                () -> System.out.println("Hata: Geçerli ödünç kaydı bulunamadı.")
        );
    }

    private void refundInvoiceProcess() {
        System.out.print("Para iadesi yapılacak kitabın ID'sini girin: ");
        long bookId = scanner.nextLong();
        scanner.nextLine();
        Optional<Loan> loanOpt = loanService.getLoanByBookId(bookId);
        if (loanOpt.isEmpty()) {
            System.out.println("Hata: Bu kitap için geçerli bir ödünç kaydı bulunamadı.");
            return;
        }

        Loan loan = loanOpt.get();
        Optional<Invoice> invoiceOpt = invoiceService.findInvoiceByLoan(loan);

        if (invoiceOpt.isEmpty()) {
            System.out.println("Hata: Bu kitap için geçerli bir fatura bulunamadı.");
            return;
        }

        Invoice invoice = invoiceOpt.get();
        if (invoice.isPaid()) {
            System.out.println("Bu kitabın ücreti zaten iade edilmiş.");
        } else {
            invoiceService.refundInvoice(scanner, loan);
        }
    }
}

