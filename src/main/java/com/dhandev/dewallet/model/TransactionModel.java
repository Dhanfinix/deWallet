package com.dhandev.dewallet.model;

import com.dhandev.dewallet.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class TransactionModel extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int trxId;

    @Column
    public String username;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @Column
    public BigDecimal amount;

    @Column
    public String status;

    @Column
    public LocalDate trxDate;

    @Column
    public BigDecimal balanceAfter;

    @Column
    public BigDecimal balanceBefore;

    @Column
    public String type;

}
