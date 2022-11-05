package com.Omer.Account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)// tüm metodlardan önce security devrede
public class SecurityConfig {

/*
    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Autowired
    UserDetailsService  userDetailsService;
*/


/*    @Bean
    PasswordEncoder bcryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder(10);
    }*/

    // Base Security

    @Bean
    protected InMemoryUserDetailsManager configureAuthentication()
    {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String passwordUser= "User1234";
        String passwordAdmin ="Admin1234";

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode(passwordUser))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode(passwordAdmin))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,admin);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/v1/account/trs").hasRole("ADMIN")
                .antMatchers("/v1/account").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/v1/customer/**").hasRole("USER")
              //  .antMatchers("/v1/customer/**").authenticated()//username@passord doğruysa girsin rollerle gerek yok
                .and().formLogin()
                .and().csrf().disable();//bir username password isteği geldiği zaman başkası o linki kullanıp giremesin arada cookie olsun.güvenlik açığı olur yapmazsak
        return http.build();

    }

/*

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // jwt security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf().disable()
              .authorizeRequests().antMatchers("/login").permitAll()
              .anyRequest().authenticated()
                      .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }



*/



/*

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
*/

}
