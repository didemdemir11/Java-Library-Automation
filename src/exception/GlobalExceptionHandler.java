package exception;

public class GlobalExceptionHandler {
    public static void handleException(Exception e) {
        if (e instanceof BookNotFoundException) {
            System.out.println("Kitap bulunamadı: " + e.getMessage());
        } else if (e instanceof InvalidInputException) {
            System.out.println("Geçersiz giriş: " + e.getMessage());
        } else if (e instanceof UnauthorizedActionException) {
            System.out.println("Yetkisiz işlem: " + e.getMessage());
        } else if (e instanceof ReaderNotFoundException) {
            System.out.println("Okuyucu bulunamadı: " + e.getMessage());
        } else if (e instanceof MaxLoanLimitExceededException) {
            System.out.println("Ödünç alma limiti aşıldı: " + e.getMessage());
        } else if (e instanceof BookAlreadyLoanedException) {
            System.out.println("Kitap zaten ödünç alınmış: " + e.getMessage());
        } else {
            System.out.println("Bilinmeyen bir hata oluştu: " + e.getMessage());
        }
    }
}