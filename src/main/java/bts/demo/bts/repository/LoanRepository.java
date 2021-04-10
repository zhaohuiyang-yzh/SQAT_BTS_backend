package bts.demo.bts.repository;

import bts.demo.bts.domain.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, String> {
    @Query("from Loan ")
    public List<Loan> listAll();

    public List<Loan> findAllByAccountNum(String accountNum);

    public Loan findByIouNum(String iouNum);
}
