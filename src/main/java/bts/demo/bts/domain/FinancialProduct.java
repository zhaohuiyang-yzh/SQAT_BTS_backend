package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class FinancialProduct {
    @Id
    private String name;
    private String timeLimit;
    private double singlePrice;
    private String type;
    private String yearlyBenefit;
}
