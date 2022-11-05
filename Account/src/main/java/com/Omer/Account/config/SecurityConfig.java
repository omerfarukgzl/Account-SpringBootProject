package com.Omer.Account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    protected InMemoryUserDetailsManager configureAuthentication()
    {
        String pass= "1234";


        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode(pass))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER", "ADMIN")
                .build();



       /* List<UserDetails> userDetailsList = new ArrayList<>();

        List<GrantedAuthority> employeeRoles = new ArrayList<>();
        employeeRoles.add(new SimpleGrantedAuthority("USER"));

        List<GrantedAuthority> adminRoles = new ArrayList<>();
        adminRoles.add(new SimpleGrantedAuthority("ADMIN"));

        userDetailsList.add( new User("admin","$2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dq",adminRoles));
        userDetailsList.add(new User("user","$2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dw",employeeRoles));
*/

        return new InMemoryUserDetailsManager(user,admin);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/v1/account/trs").hasRole("ADMIN")
                .antMatchers("/v1/account").hasRole("USER")
                .antMatchers("/v1/customer/**").hasRole("USER")
                .and().formLogin();
        return http.build();

    }


/*
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }*/

}
