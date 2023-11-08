package com.runnerpia.boot.auth.jwt;


import com.runnerpia.boot.exception.ErrorCode;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        System.out.println("requestURI : " + requestURI);
        System.out.println(!requestURI.contains("/auth/"));
        System.out.println(StringUtils.hasText(accessToken));

        System.out.println(false && false);

        if (StringUtils.hasText(accessToken) && !requestURI.contains("/auth/")) {
            System.out.println("진입함?");
            try{
                Jwts.parserBuilder()
                        .setSigningKey(jwtProvider.getKey())
                        .build()
                        .parseClaimsJws(accessToken);

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (ExpiredJwtException e){
                request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
            } catch (MalformedJwtException e){
                request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
            } catch (SignatureException e){
                request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {

        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        return null;
    }

}
