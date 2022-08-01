package com.seventeam.algoritmgameproject.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = provider.resolveToken(request);
        String requestURI = request.getRequestURI();
        log.info(requestURI);

        if (StringUtils.hasText(jwt) && provider.validateToken(jwt)) {
            Authentication authentication = provider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 {} 인증 정보를 저장했습니다", authentication.getName());
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            throw new JwtException("유효한 JWT 토큰이 없습니다");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        return isResourceUrl(request.getRequestURL().toString());
    }

    private boolean isResourceUrl(String url) {
        boolean isResourceUrl = false;
        List<String> resourceRequests = Arrays.asList(
                "/login/oauth2/code/github",
                "/ws-stomp",
                "/swagger-ui",
                "/swagger-resources",
                "/v3/api-docs",
                "/css",
                "/images",
                "/comment",
                "/js",
                "/actuator",
                "/actuator/**",
                "/h2-console",
                "/favicon.ico",
                "/profile");
        for (String resourceRequest : resourceRequests) {
            if (url.contains(resourceRequest)) {
                isResourceUrl = true;
                break;
            }
        }
        return isResourceUrl;
    }

}
