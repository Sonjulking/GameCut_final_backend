package com.gaeko.gamecut.controller;

import kr.co.youiwe.webservice.BitSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    @Autowired
    private BitSms bitSms;

    @GetMapping("/sendSms")
    public String sendSms() {
        String from = "";
        String to = "";
        String msg = "hello zito";
        bitSms.sendMsg(from, to, msg);
        return "SMS Sent Successfully";
    }
}