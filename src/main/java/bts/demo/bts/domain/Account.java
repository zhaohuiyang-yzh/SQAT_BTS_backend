package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Account {
    public static final int CREDIT_LEVEL1 = 1;
    public static final int CREDIT_LEVEL2 = 2;
    public static final int CREDIT_LEVEL3 = 3;
    public static final double LEVEL1_LIMIT = 5e5;
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
