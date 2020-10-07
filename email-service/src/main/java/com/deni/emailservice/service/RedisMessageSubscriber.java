package com.deni.emailservice.service;


import com.deni.emailservice.dto.EmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RedisMessageSubscriber implements MessageListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisMessageSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.debug("{}", message);
        String msg = message.toString();
        try {
            EmailDto emailDto = objectMapper.readValue(msg, EmailDto.class);
            emailService.sendEmail(emailDto.getTo(), emailDto.getSubjek(), emailDto.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
