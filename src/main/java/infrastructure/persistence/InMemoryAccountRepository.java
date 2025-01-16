package infrastructure.persistence;

import domain.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryAccountRepository {

    void saveAccount(Account account);

    // Récupère un compte par ID
    Account getAccountById(String id) ;
}
