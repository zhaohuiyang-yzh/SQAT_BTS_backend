package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ProductHold {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String accountNum;
    private int productName;
    private String buyingDate;
    private int count;
    private double buyingPrice;
}
