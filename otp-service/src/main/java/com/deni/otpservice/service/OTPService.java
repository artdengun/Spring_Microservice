package com.deni.otpservice.service;


import com.deni.otpservice.db.entity.TempOTP;
import com.deni.otpservice.db.repository.TempOTPRepository;
import com.deni.otpservice.dto.EmailDto;
import com.deni.otpservice.dto.RegisterCheckDto;
import com.deni.otpservice.dto.RegisterVerificationDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    public OTPService(TempOTPRepository tempOTPRepository, RedisTemplate redisTemplate, ChannelTopic channelTopic) {
        this.tempOTPRepository = tempOTPRepository;
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto){
        String email = registerCheckDto.getEmail();
        // check OTP REDIS
        TempOTP tempOTPByEmail = tempOTPRepository.getFirstByEmail(email);
        if(tempOTPByEmail!=null) {
            tempOTPRepository.delete(tempOTPByEmail);

        }
        // Generate random number
        String randomOTP  = generateOTP();
        log.debug("random otp: {}", randomOTP);


        // save to redis
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(email);
        tempOTP.setOtp(randomOTP);
        tempOTPRepository.save(tempOTP);

        // send message broker
        sendEmail(email, "AAA TAYANG UMIIIII : " + randomOTP);
    }

    private  void sendEmail(String to, String body) {
        log.debug("to: {}, body: {}", to, body);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(to);
        emailDto.setSubjek("SAYANG UMI AA TAYANGGG ");
        emailDto.setBody(body);
        redisTemplate.convertAndSend(channelTopic.getTopic(), emailDto);


    }

    private String generateOTP()  {
        return  new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

    public ResponseEntity<?> verificationOTP(RegisterVerificationDto registerVerificationDto) {
    // check by email
       TempOTP tempOTPBemail=  tempOTPRepository.getFirstByEmail(registerVerificationDto.getEmail());
       if(tempOTPBemail!= null ) return ResponseEntity.notFound().build();
       // jika datanya tidak ada maka tampilkan pesan error

        // verification otp / validasi otp
            if(tempOTPBemail.getOtp().equals(registerVerificationDto.getOtp()))
                // kita check nih apakah verificatioonya sesuai
                return ResponseEntity.unprocessableEntity().build();


        return ResponseEntity.ok().build();
    }
}
