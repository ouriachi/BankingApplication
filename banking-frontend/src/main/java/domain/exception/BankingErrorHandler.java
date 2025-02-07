package domain.exception;

public class BankingErrorHandler {

    // Centraliser la gestion des erreurs.
    public static void handle(Exception e) {
        System.out.println(e.getMessage());
    }
}
