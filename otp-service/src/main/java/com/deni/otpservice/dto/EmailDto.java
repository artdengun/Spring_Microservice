package com.deni.otpservice.dto;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class EmailDto {
    private String to;
    private String subjek;
    private String body;

}
