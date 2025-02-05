package service;

import entity.Book;
import entity.Librarian;
import exception.BookNotFoundException;
import exception.GlobalExceptionHandler;
import exception.InvalidInputException;
import exception.UnauthorizedActionException;
import repository.BookRepository;

import java.util.*;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public void save(Book book) {
        bookRepository.save(book);
    }

    public void addBookFromInput(Scanner scanner) {
        try {
            System.out.print("Kitap Adı: ");
            String title = scanner.nextLine().trim();

            System.out.print("Yazar: ");
            String author = scanner.nextLine().trim();

            System.out.print("Kategori: ");
            String category = scanner.nextLine().trim();

            System.out.print("Fiyat: ");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
                if (price <= 0) throw new InvalidInputException("Fiyat 0'dan büyük olmalıdır.");
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Hatalı fiyat girişi.");
            }

            System.out.print("Baskı Bilgisi: ");
            String edition = scanner.nextLine().trim();

            Book book = new Book(title, author, category, price, "Mevcut", edition);
            bookRepository.save(book);
            System.out.println(book.getTitle() + " adlı kitap eklendi. ID: " + book.getId());
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }

    public void getBookByIdFromInput(Scanner scanner) {
        try {
            System.out.print("Kitap ID girin: ");
            long id = scanner.nextLong();
            scanner.nextLine();
            bookRepository.findById(id)
                    .ifPresentOrElse(System.out::println, () -> {
                        throw new BookNotFoundException("Hata: Kitap bulunamadı.");
                    });
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new InvalidInputException("Hatalı giriş! Sayısal bir ID girin.");
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }
    public void getBooksByAuthorFromInput(Scanner scanner) {
        try {
            System.out.print("Yazar adını girin: ");
            String author = scanner.nextLine().trim();
            List<Book> filteredBooks = bookRepository.findByAuthor(author);
            if (filteredBooks.isEmpty()) {
                throw new BookNotFoundException("Bu yazara ait kitap bulunamadı.");
            } else {
                filteredBooks.forEach(System.out::println);
            }
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }

    public void getBooksByCategoryFromInput(Scanner scanner) {
        try {
            System.out.print("Kategori adını girin: ");
            String category = scanner.nextLine().trim();
            List<Book> filteredBooks = bookRepository.findByCategory(category);
            if (filteredBooks.isEmpty()) {
                throw new BookNotFoundException("Bu kategoriye ait kitap bulunamadı.");
            } else {
                filteredBooks.forEach(System.out::println);
            }
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }

    public void listAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            if (books.isEmpty()) {
                System.out.println("Kütüphanede kitap bulunmamaktadır.");
            } else {
                books.forEach(book -> System.out.println(book.getId() + " - " + book.getTitle() + " - " + book.getStatus()));
            }
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }
    public void updateBookDetailsFromInput(Scanner scanner, Librarian librarian) {
        try {
            if (librarian == null) {
                throw new UnauthorizedActionException("Yetkisiz işlem: Sadece kütüphane görevlileri kitap bilgilerini güncelleyebilir.");
            }

            System.out.print("Güncellenecek kitabın ID'sini girin: ");
            long id = scanner.nextLong();
            scanner.nextLine(); // Buffer temizleme

            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Belirtilen ID'ye sahip kitap bulunamadı."));

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
                    if (newPrice > 0) {
                        book.setPrice(newPrice);
                    } else {
                        throw new InvalidInputException("Hata: Fiyat 0'dan büyük olmalıdır.");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidInputException("Geçersiz fiyat girdisi. Lütfen geçerli bir sayı girin.");
                }
            }

            System.out.print("Yeni Durum (Mevcut: " + book.getStatus() + "): ");
            String newStatus = scanner.nextLine().trim();
            if (!newStatus.isEmpty()) book.setStatus(newStatus);

            bookRepository.update(book);
            System.out.println("Kitap bilgileri başarıyla güncellendi: " + book);

        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }

    public void removeBookFromInput(Scanner scanner, Librarian librarian) {
        try {
            if (librarian == null) {
                throw new UnauthorizedActionException("Yetkisiz işlem: Sadece kütüphane görevlileri kitap silebilir.");
            }

            System.out.print("Silinecek kitabın ID'sini girin: ");
            long id = scanner.nextLong();
            scanner.nextLine();
            bookRepository.findById(id)
                    .ifPresentOrElse(
                            book -> {
                                bookRepository.deleteById(id);
                                System.out.println("Kitap başarıyla silindi.");
                            },
                            () -> {
                                throw new BookNotFoundException("Belirtilen ID'ye sahip kitap bulunamadı.");
                            }
                    );
        } catch (Exception e) {
            GlobalExceptionHandler.handleException(e);
        }
    }

    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }
    public Optional<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}



