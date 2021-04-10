package bts.demo.bts.repository;

import bts.demo.bts.domain.DataUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataUserRepository extends CrudRepository<DataUser, String> {
    @Query("from DataUser ")
    public List<DataUser> listAll();

    public DataUser findByUsername(String username);
}
