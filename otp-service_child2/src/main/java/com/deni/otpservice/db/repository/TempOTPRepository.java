package com.deni.otpservice.db.repository;


import com.deni.otpservice.db.entity.TempOTP;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;


public interface TempOTPRepository  extends CrudRepository<TempOTP, String> {
    TempOTP getFirstByEmail(String email); // kita mencari data by email
}
