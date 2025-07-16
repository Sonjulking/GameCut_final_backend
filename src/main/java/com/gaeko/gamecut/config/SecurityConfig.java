// 2025-07-12 생성됨
package com.gaeko.gamecut.config;

import com.gaeko.gamecut.filter.JwtAuthenticationFilter;
import com.gaeko.gamecut.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Security 필터 체인을 설정합니다...");

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // API 경로에 맞게 수정
                    .requestMatchers("/api/user/**").permitAll()
                    .requestMatchers("/api/board/**").permitAll()
                    .requestMatchers("/api/comment/**").permitAll()
                    .requestMatchers("/api/worldcup/**").permitAll()
                    .requestMatchers("/api/ranking/**").permitAll()
                    .requestMatchers("/api/game/**").permitAll()
                    .requestMatchers("/api/debug/**").permitAll()  // 디버깅용 추가
                    .requestMatchers("/upload/**").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers("/health").permitAll()
                    // 인증이 필요한 API
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            // 2025년 7월 14일 수정됨 - API 접근 필터를 JWT 필터 전에 추가 (임시 비활성화)
            // .addFilterBefore(apiAccessFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        log.info("Security 필터 체인 설정 완료");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("CORS 설정을 구성합니다...");

        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 오리진 설정
        configuration.addAllowedOrigin("http://localhost");           // Nginx를 통한 접근
        configuration.addAllowedOrigin("http://localhost:80");        // 명시적 포트
        configuration.addAllowedOrigin("http://localhost:5173");      // Vite 개발 서버
        configuration.addAllowedOrigin("http://192.168.0.88:5173");   // 기존 IP 유지
        configuration.addAllowedOrigin("http://127.0.0.1:5173");      // 로컬 IP
        configuration.addAllowedOrigin("http://3.37.238.85"); // 기존 IP도 유지
        configuration.addAllowedOrigin("http://3.37.238.85:80"); // 기존 IP도 유지
        configuration.addAllowedOrigin("http://www.gamecut.net"); // 기존 IP도 유지
        configuration.addAllowedOrigin("http://gamecut.net"); // 기존 IP도 유지

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 자격 증명 허용 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);

        // preflight 요청 캐시 시간
        configuration.setMaxAge(3600L);

        // 노출할 헤더 설정
        configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS 설정 완료");
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}