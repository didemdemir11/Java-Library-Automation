package exception;

public class MaxLoanLimitExceededException extends RuntimeException {
    public MaxLoanLimitExceededException(String message) {
        super(message);
    }
}