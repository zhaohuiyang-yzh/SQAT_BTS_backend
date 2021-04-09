package bts.demo.bts.repository;

import bts.demo.bts.domain.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,String> {
    public Transaction findByTransactionNum(String num);
    @Query("from Transaction")
    public List<Transaction> listAll();
}
