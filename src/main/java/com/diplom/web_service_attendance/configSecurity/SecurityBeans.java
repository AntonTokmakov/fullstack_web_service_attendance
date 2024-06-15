package com.diplom.web_service_attendance.configSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/schedule/**")
                                .permitAll()
                                .requestMatchers("/error")
                                .permitAll()
                                .requestMatchers("/static/**")
                                .permitAll()
                                .requestMatchers("/app/monitor/**")
                                .hasRole("MONITOR")
                                .requestMatchers("/app/report/**")
                                .hasRole("MONITOR")
                                .requestMatchers("/app/teacher/**")
                                .hasRole("TEACHER")
                                .requestMatchers("/app/report/**")
                                .hasRole("TEACHER")
                                .requestMatchers("/app/admin/**")
                                .hasRole("ADMIN"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
