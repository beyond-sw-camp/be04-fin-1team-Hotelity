package org.iot.hotelitybackend.login.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.login.jwt.JwtUtil;
import org.iot.hotelitybackend.login.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_AUTHORIZATION;
import static org.iot.hotelitybackend.common.constant.Constant.KEY_REFRESH_TOKEN;

@RestController
@RequestMapping("/")
public class LoginController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public LoginController(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(KEY_REFRESH_TOKEN)) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String loginCode = jwtUtil.getLoginCode(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        //make new JWT
        String newAccessToken = jwtUtil.createAccessToken(loginCode, role);

        //response
        response.setHeader(KEY_AUTHORIZATION, newAccessToken);

        ResponseVO responseVO = ResponseVO.builder()
                .resultCode(HttpStatus.OK.value())
                .message("Access Token reissued")
                .build();

        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }
}
