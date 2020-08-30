package com.example.slabiak.appointmentscheduler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .antMatchers("/api/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .antMatchers("/customers/all").hasRole("ADMIN")
                .antMatchers("/providers/new").hasRole("ADMIN")
                .antMatchers("/invoices/all").hasRole("ADMIN")
                .antMatchers("/providers/all").hasRole("ADMIN")
                .antMatchers("/customers/new/**").permitAll()
                .antMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers("/providers/availability/**").hasRole("PROVIDER")
                .antMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
                .antMatchers("/works/**").hasRole("ADMIN")
                .antMatchers("/exchange/**").hasRole("CUSTOMER")
                .antMatchers("/appointments/new/**").hasRole("CUSTOMER")
                .antMatchers("/appointments/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .antMatchers("/invoices/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/perform_logout")
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}