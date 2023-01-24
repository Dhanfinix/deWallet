package com.dhandev.dewallet.repository;

import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends CrudRepository<TransactionModel, Integer> {

    List<TransactionModel> findAllByTrxDate(LocalDate createdDate);
}
