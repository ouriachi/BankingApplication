package domain.model;

/**
 * Enum pour les types de comptes
 */
public enum AccountType {
    CURRENT_ACCOUNT(1, "Current Account"),
    SAVINGS_ACCOUNT(2, "Savings Account");

    private final int code;
    private final String description;

    AccountType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
