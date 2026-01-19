package com.efraude.config;

import com.efraude.security.AuthenticationSuccessHandler;
import com.efraude.security.CustomUserDetailsService;
import com.efraude.security.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public web pages
                .requestMatchers("/web/home", "/web/", "/web/frauds/**", "/web/about", "/web/auth/login", "/web/auth/signup", "/web/auth/confirm", "/web/auth/resend").permitAll()
                // Public API endpoints (read-only)
                .requestMatchers("/api/frauds/search", "/api/frauds/{id}").permitAll()
                // Auth endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/robots.txt", "/sitemap.xml").permitAll()
                // Admin pages
                .requestMatchers("/web/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/web/auth/login")
                .loginProcessingUrl("/web/auth/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/web/auth/login?error=true")
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/web/auth/login")
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                .successHandler(authenticationSuccessHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/web/auth/logout")
                .logoutSuccessUrl("/web/home")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
