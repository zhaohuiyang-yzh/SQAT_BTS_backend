package bts.demo.bts.repository;

import bts.demo.bts.domain.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanRepository extends CrudRepository<Plan, Integer> {
    public List<Plan> findAllByIouNum(String iouNum);

    public Plan findByIouNumAndPlanNum(String iouNum, int planNum);

    @Query("from Plan ")
    public List<Plan> listAll();
}
