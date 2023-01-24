package com.dhandev.dewallet.model;

import com.dhandev.dewallet.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class TransactionModel extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int trxId;

    @Column
    public String username;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @Column
    public BigDecimal amount;

    @Column
    public String status;


}
