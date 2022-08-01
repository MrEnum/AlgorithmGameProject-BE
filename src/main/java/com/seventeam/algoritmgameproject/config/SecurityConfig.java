package com.seventeam.algoritmgameproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seventeam.algoritmgameproject.security.JwtExceptionFilter;
import com.seventeam.algoritmgameproject.security.JwtFilter;
import com.seventeam.algoritmgameproject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity//(debug = true)  // 기본적인 웹보안 활성화
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) // 서블릿 빈 등록
public class SecurityConfig {
    private final JwtTokenProvider provider;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    // 비밀번호 인코딩
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        //swagger 외부 접근
        //http.headers().frameOptions().sameOrigin();
        http
                .csrf().disable()   // 토큰 방식 사용하기 때문에 csrf 설정 비활성
                // H2-console 을 위한 설정
                .headers().frameOptions().disable()

                .and()  // 세션 사용하지 않기 때문에 무상태로 변경
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()   // JwtFilter 를 등록한 JwtSecurityConfig 적용
                .addFilterBefore(new JwtFilter(provider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(new ObjectMapper()), JwtFilter.class);
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/login/oauth2/code/github").permitAll()
//                .antMatchers(HttpMethod.GET, "/ws-stomp/**").permitAll()
//                .antMatchers(
//                        "/swagger-ui/**"
//                        , "/swagger-resources/**"
//                        , "/v3/api-docs").permitAll()
//                .antMatchers("/css/**", "/images/**", "/comment/**",
//                        "/js/**", "/h2-console/**", "/favicon.ico").permitAll()
//                .anyRequest().authenticated();


        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("https://chinda.live");
        configuration.addAllowedOrigin("https://www.chinda.live");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
