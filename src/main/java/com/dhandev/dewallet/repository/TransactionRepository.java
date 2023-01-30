package com.dhandev.dewallet.repository;

import com.dhandev.dewallet.model.TransactionModel;
import org.springframework.data.repository.CrudRepository;


public interface TransactionRepository extends CrudRepository<TransactionModel, Integer> {

}
