package com.Omer.Account.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

/*    @Autowired
    private JavaMailSender mailSender;*/

    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "transfer-notification",
            groupId = "group-id")
    public void consume(String message){

        emailService.sendSimpleMessage("saupay54@gmail.com","guzelomerfaruk9@gmail.com","Merhaba Omer",message);

       // logger.info(String.format("Message receiver \n %s", message));
    }
}


