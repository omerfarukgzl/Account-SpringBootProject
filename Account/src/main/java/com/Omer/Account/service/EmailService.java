package com.Omer.Account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

       // String from = "saupay54@gmail.com";
        //String to = "guzelomerfaruk9@gmail.com";
        message.setFrom(from);
        message.setTo(to);

        message.setSubject(subject);
        message.setText(text);
        //SimpleMailMessage message = new SimpleMailMessage();
        emailSender.send(message);

    }

}