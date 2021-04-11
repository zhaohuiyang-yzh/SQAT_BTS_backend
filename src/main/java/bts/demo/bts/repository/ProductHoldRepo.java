package bts.demo.bts.repository;
import bts.demo.bts.domain.ProductHold;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductHoldRepo extends CrudRepository<ProductHold, Integer> {
   public List<ProductHold> findAllByAccountNum(String accountNum);
}
