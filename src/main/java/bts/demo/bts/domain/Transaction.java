package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Transaction {
    @Id
    private String transactionNum;
    private String branchName;
    private String transactionType;
    private Double amount;
    private String account;
    private String transactionCode;
    private String operatorTime;


}
