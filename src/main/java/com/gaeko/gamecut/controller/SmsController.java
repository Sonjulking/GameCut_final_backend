package com.gaeko.gamecut.controller;

import kr.co.youiwe.webservice.BitSms;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class SmsController {
    @Autowired
    private BitSms bitSms;
    
    @GetMapping("/sendSms")
    public String sendSms() {
        String from = "01024475948";
        String to = "01024475948";
        String msg = "sms test";
        bitSms.sendMsg(from, to, msg);
        return "SMS Sent Successfully";
    }

    
}