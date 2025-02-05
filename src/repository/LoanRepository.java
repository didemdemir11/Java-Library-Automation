package repository;

import entity.Book;
import entity.Loan;
import entity.Reader;
import service.LoanRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository {
    private final List<Loan> loans = new ArrayList<>();
    private final List<LoanRecord> loanRecords = new ArrayList<>();
    private final List<Reader> readers = new ArrayList<>();
    public void saveLoan(Loan loan) {
        loans.add(loan);
    }

    public void deleteLoan(Loan loan) {
        loans.remove(loan);
    }

    public Optional<Loan> findLoanByBookId(long bookId) {
        return loans.stream()
                .filter(loan -> loan.getBook().getId() == bookId)
                .findFirst();
    }

    public Optional<Loan> findLoanByBookAndReader(Book book, Reader reader) {
        return loans.stream()
                .filter(loan -> loan.getBook().equals(book) && loan.getReader().equals(reader))
                .findFirst();
    }

    public List<Loan> findLoansByReader(Reader reader) {
        return loans.stream()
                .filter(loan -> loan.getReader().equals(reader))
                .toList();
    }

    public void saveLoanRecord(LoanRecord loanRecord) {
        loanRecords.add(loanRecord);
    }

    public List<LoanRecord> findAllLoanRecords() {
        return new ArrayList<>(loanRecords);
    }
    public Optional<Reader> findReaderByEmail(String email) {
        return readers.stream()
                .filter(reader -> reader.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
    public void saveReader(Reader reader) {
        if (findReaderByEmail(reader.getEmail()).isEmpty()) { //Aynı okuyucu tekrar eklenmesin
            readers.add(reader);
        }
    }
}
