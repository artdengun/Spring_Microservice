package com.deni.otpservice.controller;


import com.deni.otpservice.dto.RegisterCheckDto;
import com.deni.otpservice.dto.RegisterVerificationDto;
import com.deni.otpservice.service.OTPService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class OtpController {
    private final OTPService otpService;
    private final Environment environment;

    @Autowired
    public OtpController(OTPService otpService, Environment environment) {
        this.otpService = otpService;
        this.environment = environment;
    }

    @Value("${db.string:default}")
    private String dbString;

    @PostMapping("/request")
    public ResponseEntity<?>  requestOTP(@RequestBody RegisterCheckDto registerCheckDto) {
        log.debug("request OTP:  {}", registerCheckDto);
        otpService.requestOTP(registerCheckDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/test-balancer")
    public String testLoadBalancer() {
        String port = environment.getProperty("local.server.port");
        log.debug("port: {}", port);
        return port;
    }
    @PostMapping("/verification")
    public ResponseEntity<?> verificationOTP(@RequestBody RegisterVerificationDto registerVerificationDto)  {
        return otpService.verificationOTP(registerVerificationDto);
    }

    @GetMapping("/test-profil-config")
    public String testProfileConfig() {
        return dbString;
    }
}
