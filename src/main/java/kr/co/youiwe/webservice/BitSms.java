package kr.co.youiwe.webservice;

import org.springframework.beans.factory.annotation.Value;

public class BitSms {
    //너나우리
    @Value("${send.sms.id}")
    private String smsID;
    @Value("${send.sms.password}")
    private String smsPW;

    public void sendMsg(String from, String to, String msg) {


        ServiceSMSSoapProxy sendsms = new ServiceSMSSoapProxy();
        try {
            String senderPhone = from;
            String receivePhone = to;
            String smsContent = msg;
            String test1 = (smsID + smsPW + receivePhone);
            CEncrypt encrypt = new CEncrypt("MD5", test1);
            String send = sendsms.sendSMS(smsID, encrypt.getEncryptData(), senderPhone, receivePhone, smsContent);
            System.out.println("결과코드:" + send);
        } catch (Exception e) {
            System.out.println("Exception in main:" + e);
        }
    }
}
