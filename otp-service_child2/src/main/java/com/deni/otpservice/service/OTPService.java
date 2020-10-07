package com.deni.otpservice.service;


import com.deni.otpservice.db.entity.TempOTP;
import com.deni.otpservice.db.repository.TempOTPRepository;
import com.deni.otpservice.dto.RegisterCheckDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;

    public OTPService(TempOTPRepository tempOTPRepository) {
        this.tempOTPRepository = tempOTPRepository;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto){
        String email = registerCheckDto.getEmail();
        // check OTP REDIS
        TempOTP tempOTPByEmail = tempOTPRepository.getFirstByEmail(email);
        if(tempOTPByEmail!=null) {
            tempOTPRepository.delete(tempOTPByEmail);

        }
        // Generate random number  / otp
        String randomOTP  = generateOTP();
        log.debug("random otp: {}", randomOTP);
        // save to redis
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(email);
        tempOTP.setOtp(randomOTP);
            tempOTPRepository.save(tempOTP);
    }

    private String generateOTP()  {
        return  new DecimalFormat("0000").format(new Random().nextInt(9999));
    }
}
