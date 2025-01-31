package entity;

import java.time.LocalDate;

public class Book extends BaseEntity{
    private String title;
    private String author;
    private String category;
    private double price;
    private String status;
    private String edition;
    private LocalDate purchaseDate;

    public Book(String title,String author,String category,double price, String status,String edition,LocalDate purchaseDate){
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.status = status;
        this.edition = edition;
        this.purchaseDate = purchaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getEdition() {
        return edition;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
