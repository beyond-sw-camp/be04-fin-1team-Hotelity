package org.iot.hotelitybackend.login.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iot.hotelitybackend.login.aggregate.RefreshToken;
import org.iot.hotelitybackend.login.jwt.JwtUtil;
import org.iot.hotelitybackend.login.repository.RefreshTokenRepository;
import org.iot.hotelitybackend.login.vo.RequestLogin;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import static org.iot.hotelitybackend.common.constant.Constant.*;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        RequestLogin loginVO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginVO = objectMapper.readValue(messageBody, RequestLogin.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String employCodeWithBranchCode = loginVO.getBranchCode() + "_" + loginVO.getEmployeeCode();
        String password = loginVO.getEmployPassword();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(employCodeWithBranchCode, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {

        /* Authentication
        - Principal : 유저에 대한 정보
        - Credentials : 증명 (비밀번호, 토큰)
        - Authorities : 유저의 권한(ROLE) 목록
        * */

        /* get employee info */
        String employeeCodeWithBranchCode = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        /* generate token */
        String accessToken = jwtUtil.createAccessToken(employeeCodeWithBranchCode, role);
        String refreshToken = jwtUtil.createRefreshToken(accessToken);

        if (refreshTokenRepository.existsById(employeeCodeWithBranchCode)) {
            refreshTokenRepository.deleteById(employeeCodeWithBranchCode);
        }

        /* save refresh token (Redis) */
        RefreshToken refreshTokenEntity = new RefreshToken(refreshToken, accessToken, employeeCodeWithBranchCode);
        refreshTokenRepository.save(refreshTokenEntity);

        /* set response */
        response.setHeader(KEY_AUTHORIZATION, accessToken);
        response.addCookie(createCookie(KEY_REFRESH_TOKEN, refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(REDIS_TIME_TO_LIVE);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
