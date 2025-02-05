package service;

import entity.Book;
import entity.Librarian;

import java.util.*;

public class BookService {
    private final Map<Long, Book> books;

    public BookService() {
        this.books = new HashMap<>();
    }

    public void addBookFromInput(Scanner scanner) {
        System.out.print("Kitap Adı: ");
        String title = scanner.nextLine().trim();

        System.out.print("Yazar: ");
        String author = scanner.nextLine().trim();

        System.out.print("Kategori: ");
        String category = scanner.nextLine().trim();

        System.out.print("Fiyat: ");
        double price = 0;
        try {
            price = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Hatalı fiyat girişi. Kitap eklenemedi.");
            return;
        }

        System.out.print("Baskı Bilgisi: ");
        String edition = scanner.nextLine().trim();

        Book book = new Book(title, author, category, price, "Mevcut", edition);
        books.put(book.getId(), book);
        System.out.println(book.getTitle() + " adlı kitap eklendi. ID: " + book.getId());
    }

    public void getBookByIdFromInput(Scanner scanner) {
        System.out.print("Kitap ID girin: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine(); // Buffer temizleme
            books.getOrDefault(id, null);
            System.out.println(books.containsKey(id) ? books.get(id) : "Hata: Kitap bulunamadı.");
        } catch (InputMismatchException e) {
            System.out.println("Hatalı giriş! Sayısal bir ID girin.");
            scanner.nextLine();
        }
    }
    public void getBooksByAuthorFromInput(Scanner scanner) {
        System.out.print("Yazar adını girin: ");
        String author = scanner.nextLine().trim();
        List<Book> filteredBooks = books.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
        if (filteredBooks.isEmpty()) {
            System.out.println("Hata: Bu yazara ait kitap bulunamadı.");
        } else {
            filteredBooks.forEach(System.out::println);
        }
    }

    public void getBooksByCategoryFromInput(Scanner scanner) {
        System.out.print("Kategori adını girin: ");
        String category = scanner.nextLine().trim();
        List<Book> filteredBooks = books.values().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
        if (filteredBooks.isEmpty()) {
            System.out.println("Hata: Bu kategoriye ait kitap bulunamadı.");
        } else {
            filteredBooks.forEach(System.out::println);
        }
    }
    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("Kütüphanede kitap bulunmamaktadır.");
        } else {
            books.values().forEach(book -> System.out.println(book.getId() + " - " + book.getTitle() + " - " + book.getStatus()));
        }
    }
    public void updateBookDetailsFromInput(Scanner scanner, Librarian librarian) {
        if (librarian == null) {
            System.out.println("Yetkisiz işlem: Sadece kütüphane görevlileri kitap bilgilerini güncelleyebilir.");
            return;
        }

        System.out.print("Güncellenecek kitabın ID'sini girin: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine(); // Buffer temizleme

            if (!books.containsKey(id)) {
                System.out.println("Hata: Belirtilen ID'ye sahip kitap bulunamadı.");
                return;
            }

            Book book = books.get(id);
            System.out.println("Mevcut Kitap Bilgileri: " + book);
            System.out.println("Güncellemek istemediğiniz alanları boş bırakabilirsiniz.");

            System.out.print("Yeni Başlık: ");
            String newTitle = scanner.nextLine().trim();
            if (!newTitle.isEmpty()) book.setTitle(newTitle);

            System.out.print("Yeni Yazar: ");
            String newAuthor = scanner.nextLine().trim();
            if (!newAuthor.isEmpty()) book.setAuthor(newAuthor);

            System.out.print("Yeni Kategori: ");
            String newCategory = scanner.nextLine().trim();
            if (!newCategory.isEmpty()) book.setCategory(newCategory);

            System.out.print("Yeni Fiyat (Mevcut: " + book.getPrice() + "): ");
            String priceInput = scanner.nextLine().trim();
            if (!priceInput.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceInput);
                    if (newPrice > 0) book.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz fiyat girdisi. Güncelleme yapılmadı.");
                }
            }

            System.out.print("Yeni Durum (Mevcut: " + book.getStatus() + "): ");
            String newStatus = scanner.nextLine().trim();
            if (!newStatus.isEmpty()) book.setStatus(newStatus);

            System.out.println("Kitap bilgileri güncellendi: " + book);
        } catch (InputMismatchException e) {
            System.out.println("Hata: Geçersiz giriş! Lütfen sayısal bir ID girin.");
            scanner.nextLine(); // Hatalı girdiyi temizleme
        }
    }

    public void removeBookFromInput(Scanner scanner, Librarian librarian) {
        if (librarian == null) {
            System.out.println("Yetkisiz işlem: Sadece kütüphane görevlileri kitap silebilir.");
            return;
        }

        System.out.print("Silinecek kitabın ID'sini girin: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            if (books.containsKey(id)) {
                books.remove(id);
                System.out.println("Kitap başarıyla silindi.");
            } else {
                System.out.println("Hata: Belirtilen ID'ye sahip kitap bulunamadı.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Hata: Geçersiz giriş! Sayısal bir ID girin.");
            scanner.nextLine();
        }
    }
    public Optional<Book> getBookById(long id) {
        return Optional.ofNullable(books.get(id));
    }
    public Optional<List<Book>> getBooksByAuthor(String author) {
        List<Book> filteredBooks = books.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
        return filteredBooks.isEmpty() ? Optional.empty() : Optional.of(filteredBooks);
    }

    public Optional<List<Book>> getBooksByCategory(String category) {
        List<Book> filteredBooks = books.values().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
        return filteredBooks.isEmpty() ? Optional.empty() : Optional.of(filteredBooks);
    }
    public void addBook(Book book) {
        if (book == null) {
            System.out.println("Hata: Geçersiz kitap nesnesi!");
            return;
        }
        books.put(book.getId(), book);
        System.out.println("Kitap başarıyla eklendi: " + book.getTitle());
    }
    public Optional<Book> getBookByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

}
