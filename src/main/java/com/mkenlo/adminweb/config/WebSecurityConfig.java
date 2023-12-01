package com.mkenlo.adminweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(
                (authz)-> authz
                        .requestMatchers("/","/home").permitAll()
                        .requestMatchers("/customers/**").hasRole("USER")
                        .requestMatchers("/orders").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                                .failureUrl("/login?error")
                                .permitAll()
                        )
                .logout(form -> form.clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                        );
        return http.build();
    }
    @Bean
    public UserDetailsService users(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper(){
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToLowerCase(true);
        return authorityMapper;
    }

}
