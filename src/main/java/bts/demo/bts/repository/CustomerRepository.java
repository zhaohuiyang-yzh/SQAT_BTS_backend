package bts.demo.bts.repository;

import bts.demo.bts.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {
    @Query("from Customer ")
    public List<Customer> listAll();
}
