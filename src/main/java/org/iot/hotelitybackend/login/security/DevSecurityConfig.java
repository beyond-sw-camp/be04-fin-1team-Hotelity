package org.iot.hotelitybackend.login.security;

import org.iot.hotelitybackend.login.jwt.DevJwtFilter;
import org.iot.hotelitybackend.login.jwt.JwtUtil;
import org.iot.hotelitybackend.login.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static org.iot.hotelitybackend.common.constant.Constant.CORS_EXPOSED_HEADER_SET_COOKIE;
import static org.iot.hotelitybackend.common.constant.Constant.KEY_AUTHORIZATION;

@Profile("dev")
@Configuration
@EnableWebSecurity(debug = true)
public class DevSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public DevSecurityConfig(
            JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository,
            AuthenticationConfiguration authenticationConfiguration
    ) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configureSecurityFilterChainDev(HttpSecurity http) throws Exception {

        // configure CORS
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> {

            List<String> allowStringList = Collections.singletonList("*");
            List<String> exposedHeaders =
                    List.of(KEY_AUTHORIZATION, CORS_EXPOSED_HEADER_SET_COOKIE);
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOriginPatterns(allowStringList);
            configuration.setAllowedMethods(allowStringList);
            configuration.setAllowedHeaders(allowStringList);
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(exposedHeaders);

            return configuration;
        }));

        // csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        // form login disable
        http.formLogin(AbstractHttpConfigurer::disable);

        // http basic disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
                .anyRequest().permitAll()
        );

        http.addFilterBefore(new DevJwtFilter(), AuthenticationFilter.class);

        AuthenticationFilter loginFilter = new AuthenticationFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository);

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new UserLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        // logout
        http.logout(LogoutConfigurer::permitAll);

        // 세션 설정
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
