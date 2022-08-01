package com.seventeam.algoritmgameproject.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seventeam.algoritmgameproject.domain.model.login.User;
import com.seventeam.algoritmgameproject.web.service.login_service.UserDetailImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITIES_KEY = "auth";
    private final ObjectMapper mapper;
    private final Key key;
    private static final long TOKEN_VALID_TIME = 1000L*60*30*2*5; //5시간

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        this.mapper = new ObjectMapper();
    }

    public String createToken(User user){
        try {
            String detail = mapper.writeValueAsString(user);
            Date now = new Date();
            return "Bearer " + Jwts.builder()   // 토큰 생성해서 리턴
                    .setSubject(user.getUserId())
                    .claim(AUTHORITIES_KEY, detail)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
                    .compact();
        }catch (JsonProcessingException e){
            throw new RuntimeException("토큰 생성 실패");
        }

    }


    // Token 에 담겨있는 정보를 이용 Authentication 객체 리턴
    public Authentication getAuthentication(String token) throws JsonProcessingException {
        Claims claims = Jwts    // token 을 이용해 Claims 만듬
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String user = claims.get(AUTHORITIES_KEY).toString();
        UserDetailImpl userDetail = new UserDetailImpl(mapper.readValue(user,User.class));
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(BEARER_TYPE.length());
        }
        return null;
    }

    // Token 유효성 검증 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
    }
}
