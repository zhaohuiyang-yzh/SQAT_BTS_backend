package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Plan {
    public static final int UN_REPAY = 1;
    public static final int HAVE_PAYED = 2;
    public static final int OVERDUE = 3;
    public static final double PENALTY_RATE = 0.05;
    @Id
    private int id;
    private int planNum;
    private int creator;
    private double penaltyInterest;
    private String iouNum;
    private String date;
    private double principal;
    private int repaymentStatus;
    private double interest;
    private double amount;
    private double remainAmount;
    private double remainInterest;
    private int payMethod;
    private double remainPrincipal;
}
