package bts.demo.bts.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DataUser {
    @Id
    private String username;
    private String password;
}
