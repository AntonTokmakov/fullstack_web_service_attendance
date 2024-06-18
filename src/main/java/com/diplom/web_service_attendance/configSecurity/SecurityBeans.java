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
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/schedule/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/static/**").permitAll()

                                .requestMatchers("/app/report/**").hasRole("MONITOR")
                                .requestMatchers("/app/report/**").hasRole("TEACHER")
                                .requestMatchers("/app/monitor/**").hasRole("MONITOR")
                                .requestMatchers("/app/teacher/**").hasRole("TEACHER")
                                .requestMatchers("/app/admin/**").hasRole("ADMIN")
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
