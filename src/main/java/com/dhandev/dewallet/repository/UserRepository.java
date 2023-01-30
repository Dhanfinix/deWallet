package com.dhandev.dewallet.repository;

import com.dhandev.dewallet.model.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserModel, Integer>{
    //https://www.baeldung.com/spring-data-derived-queries read more here
    List<UserModel> findByUsernameEquals(String name);      //Derived Query, otomatis buat query berdasar nama method
    List<UserModel> findByKtpEquals(String ktp);
    UserModel findByUsername(String name);

//    @Query(nativeQuery = true, value = "select tm.* from usermodel.user_model um left join usermodel.transaction_model tm on um.id = tm.user_id where tm.trx_date = ?1")
//    List<Object> findByTrxDate(LocalDate trx_date);
}
