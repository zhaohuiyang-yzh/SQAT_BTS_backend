package bts.demo.bts.repository;

import bts.demo.bts.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    public List<Account> findAllByCustomerId(int customerId);

    public Account findByAccountNum(String accountNum);
}
