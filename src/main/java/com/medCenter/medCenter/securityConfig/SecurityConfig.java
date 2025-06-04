package com.medCenter.medCenter.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public UserDetailsServiceImpl authDetailsService (){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder(4);
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {  //!!!!!!!!!!!!!!!!! for correction!
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authDetailsService());
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/personal", "/registration", "/login").permitAll()
                        .requestMatchers( "client/**" ).hasAuthority("ROLE_CLIENT")
                        .requestMatchers( "admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers( "doctor/**").hasAuthority("ROLE_DOCTOR")
                        .requestMatchers( "medCard/**").hasAnyAuthority("ROLE_DOCTOR","ROLE_CLIENT","ROLE_NURSE")
                        .requestMatchers( "nurse/**").hasAuthority("ROLE_NURSE")
                        .requestMatchers("/images/**", "/css/**").permitAll()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .loginPage("/login")
                        .successHandler(new AuthenticationSuccessHandler())
                )
                .logout(logout->logout
                        .logoutUrl("/exit")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();


    }


}
