package com.npn.spring.learning.spring.smallspringboot.configurations;

import com.npn.spring.learning.spring.smallspringboot.model.security.servises.MyAuthenticationSuccessHandler;
import com.npn.spring.learning.spring.smallspringboot.model.security.servises.MyDatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${my.session.timeout}")
    private int MAX_INACTIVE_INTERVAL;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/home").permitAll()
                .anyRequest().authenticated().and()
             .formLogin().loginPage("/login").successHandler(new MyAuthenticationSuccessHandler(MAX_INACTIVE_INTERVAL))
                .permitAll().and()
             .logout()
                .permitAll()
                .and()
             .httpBasic();
        http.sessionManagement().maximumSessions(1).expiredUrl("/login");
    }



    /**
     * @return the {@link UserDetailsService} to use
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
        return new MyDatabaseUserDetailsService();
    }

    /**
     * Временная реализация-заместитель проверки пароля
     * @return
     */
    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

}
