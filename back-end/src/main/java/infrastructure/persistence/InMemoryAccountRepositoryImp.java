package infrastructure.persistence;

import domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryAccountRepositoryImp implements InMemoryAccountRepository {

    private final Map<String, Account> accounts = new HashMap<>();

    // Sauvegarde un compte
    public void saveAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    // Récupère un compte par ID
    public Account getAccountById(String id) {
        return accounts.get(String.valueOf(id));
    }
}
