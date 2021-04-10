package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Account {
    @Id
    private int id;
    private int customerId;
    private int creator;
    private String accountNum;
    private int voucherType;
    private int accountType;
    private String updateTime;
    private String accountTypeName;
    private String password;
    private String voucherName;
    private double balance;
    private String createTime;
    private int loanProduct;
    private int state;
}
