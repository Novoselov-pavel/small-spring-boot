package com.npn.spring.learning.spring.smallspringboot.configurations;

import com.npn.spring.learning.spring.smallspringboot.model.security.servises.MyAuthenticationSuccessHandler;
import com.npn.spring.learning.spring.smallspringboot.model.security.servises.MyDatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Конфигурационный класс для разграничения доступа
 */
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${my.session.timeout}")
    private int MAX_INACTIVE_INTERVAL;

    /**
     * Конфигурация Spring Security
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/","/home","/registry","/static/**", "/error")
                .anonymous()
                .antMatchers(HttpMethod.POST,"/registry")
                .anonymous()
                .antMatchers("/","/home","/registry","/static/**")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/registry")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(new MyAuthenticationSuccessHandler(MAX_INACTIVE_INTERVAL))
                .permitAll().and()
                .logout()
                .permitAll().deleteCookies("JSESSIONID")
                .and().httpBasic().and()
                .sessionManagement() // реализация позможности захода только с одного адреса,для корректной работы нужно в пользователе
                                     // правильно реальзовать equals и hashCode
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login")
                .sessionRegistry(sessionRegistry());
    }



    /**
     * Создание бина службы, для работы с пользователем
     *
     * @return имплементацию {@link UserDetailsService}
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new MyDatabaseUserDetailsService();
    }

    /**
     * Предоставление шифровальщика паролей BCrypt
     * @return PasswordEncoder
     */
    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Регистрация реестра сеансов, для осуществления возможности входа одновременно только с одного адреса
     * @return SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    /**
     * Слушатель сессий, для осуществления возможности входа одновременно только с одного адреса
     *
     * @return ServletListenerRegistrationBean
     */
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }



}
