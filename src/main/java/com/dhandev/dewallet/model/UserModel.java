package com.dhandev.dewallet.model;

import com.dhandev.dewallet.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
public class UserModel extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public UserModel(){

    }

    public UserModel(String username, String password, String oldPassword, String ktp, boolean banned) {
        this.username = username;
        this.password = password;
        this.oldPassword = oldPassword;
        this.ktp = ktp;
        this.banned = banned;
    }

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String oldPassword;

    @Column
    private String ktp;

    @Column
    private boolean banned;

    @Column
    private BigDecimal balance;

    @Column
    private BigDecimal transactionLimit;

    @OneToMany(mappedBy = "userModel")
    private List<TransactionModel> transaction;

    @Column
    private int passwordAttempt = 0;
}
