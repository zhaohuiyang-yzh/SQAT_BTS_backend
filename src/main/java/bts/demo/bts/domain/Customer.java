package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Customer {
    @Id
    private int id;
    private String address;
    private String alterNateName;
    private String alterNatePhone;
    private String branchNum;
    private String code;
    private String createTime;
    private int creator;
    private String email;
    private String idNum;
    private int idType;
    private String name;
    private String permanentAddress;
    private String phone;
    private int sex;
    private String updateTime;
    private int updater;

}
