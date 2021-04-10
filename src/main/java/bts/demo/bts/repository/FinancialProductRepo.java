package bts.demo.bts.repository;

import bts.demo.bts.domain.FinancialProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FinancialProductRepo extends CrudRepository<FinancialProduct, String> {
    @Query("from FinancialProduct ")
    public List<FinancialProduct> listAll();
}
