package com.gaeko.gamecut.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.UserRepository;

import kr.co.youiwe.webservice.BitSms;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsService {
	
	
	@Autowired
	private BitSms bitSms;  // SMS 발송 객체
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public boolean findPasswordByPhone(String userId, String phone) {
	    Optional<User> userOpt = userRepository.findByUserIdAndPhone(userId, phone);
	    if (userOpt.isEmpty()) return false;

	    String tempPwd = generateTempPassword();
	    userOpt.get().setUserPwd(passwordEncoder.encode(tempPwd));
	    userRepository.save(userOpt.get());

	    // 문자 발송
	    String to = phone;
	    String from = "01012345678"; // 보내는 번호 (가입된 인증된 번호)
	    String msg = "임시 비밀번호: " + tempPwd;
	    bitSms.sendMsg(from, to, msg);

	    return true;
	}
	
	
	private String generateTempPassword() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < 10; i++) {
	        int idx = (int) (Math.random() * chars.length());
	        sb.append(chars.charAt(idx));
	    }
	    return sb.toString();
	}




}
