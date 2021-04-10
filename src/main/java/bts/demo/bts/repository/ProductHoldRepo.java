package bts.demo.bts.repository;

import bts.demo.bts.domain.ProductHold;
import org.springframework.data.repository.CrudRepository;

public interface ProductHoldRepo extends CrudRepository<ProductHold, Integer> {
}
