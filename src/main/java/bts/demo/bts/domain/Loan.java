package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Loan {
    @Id
    private String iouNum;
    private int repaymentMethod;
    private int creator;
    private String customerCode;
    private String customerName;
    private String productName;
    private double totalAmount;
    private String productCode;
    private double balance;
    private String createTime;
    private double overdueBalance;
    private int loanStatus;
    private double totalInterest;
    private String accountNum;
}
