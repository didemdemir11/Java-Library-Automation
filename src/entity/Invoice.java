package entity;


import java.time.LocalDate;

public class Invoice extends BaseEntity{

    private Loan loan;
    private double amount;
    private LocalDate issueDate;
    private boolean isPaid;

    public Invoice(Loan loan, double amount, LocalDate issueDate, boolean isPaid) {
        this.loan = loan;
        this.amount = amount;
        this.issueDate = issueDate;
        this.isPaid = false;
    }

    public Loan getLoan() {
        return loan;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }
    public void markAsPaid() {
        this.isPaid = true;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
