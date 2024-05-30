package org.iot.hotelitybackend.login.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.login.vo.LoginUserDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.iot.hotelitybackend.common.constant.Constant.KEY_AUTHORIZATION;
import static org.iot.hotelitybackend.common.constant.Constant.MESSAGE_TOKEN_EXPIRED;

@Profile("staging")
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    public JwtFilter(JwtUtil jwtUtil, EmployeeRepository employeeRepository) {
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = request.getHeader(KEY_AUTHORIZATION);

        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.split("Bearer ")[1];

        try {
            if (jwtUtil.isExpired(accessToken)) {
                response.getWriter().print(MESSAGE_TOKEN_EXPIRED);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (ExpiredJwtException e) {
            response.getWriter().print(MESSAGE_TOKEN_EXPIRED);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String loginCode = jwtUtil.getLoginCode(accessToken);
        Integer employeeCode = Integer.valueOf(loginCode.split("_")[1]);
        EmployeeEntity employee = employeeRepository.findById(employeeCode).orElseThrow();
        LoginUserDetails loginUserDetails = new LoginUserDetails(employee);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUserDetails, null, loginUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
