package com.pro.scm.config;

import com.pro.scm.services.SecurityCustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Autowired
    private oAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;


   @Bean
    public DaoAuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       authProvider.setUserDetailsService(userDetailsService);
       authProvider.setPasswordEncoder(passwordEncoder());
       return authProvider;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       http.authorizeHttpRequests(authorizeRequests -> {

           authorizeRequests.requestMatchers("/user/**").authenticated()
                   .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
           ;
           authorizeRequests.anyRequest().permitAll();

       });

       http.formLogin(formLogin -> {
           formLogin.loginPage("/login");
           formLogin.loginProcessingUrl("/authenticate");
           formLogin.defaultSuccessUrl("/user/profile");
           formLogin.failureUrl("/login?error=true");
           formLogin.usernameParameter("email");
           formLogin.passwordParameter("password");


       });




       http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

       http.oauth2Login(oauth2Login -> {
           oauth2Login.loginPage("/login");
           oauth2Login.successHandler(oAuthAuthenticationSuccessHandler);
       });
       http.logout(logout -> {
           logout.logoutUrl("/logout");
       });
       return http.build();
   }
   @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }



}
