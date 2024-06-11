package org.iot.hotelitybackend.login.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iot.hotelitybackend.login.aggregate.RefreshToken;
import org.iot.hotelitybackend.login.jwt.JwtUtil;
import org.iot.hotelitybackend.login.repository.RefreshTokenRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_REFRESH_TOKEN;
import static org.iot.hotelitybackend.common.constant.Constant.MESSAGE_TOKEN_EXPIRED;

public class UserLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(KEY_REFRESH_TOKEN)) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtUtil.isExpired(refreshToken)) {
                response.getWriter().print(MESSAGE_TOKEN_EXPIRED);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (ExpiredJwtException e) {
            response.getWriter().print(MESSAGE_TOKEN_EXPIRED);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (isExist == null || !isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
        refreshTokenRepository.deleteById(tokenEntity.getLoginCode());

        Cookie cookie = new Cookie(KEY_REFRESH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
