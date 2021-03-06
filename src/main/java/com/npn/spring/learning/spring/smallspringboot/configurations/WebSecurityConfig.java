package com.npn.spring.learning.spring.smallspringboot.configurations;

import com.npn.spring.learning.spring.smallspringboot.model.security.services.MyAuthenticationSuccessHandler;
import com.npn.spring.learning.spring.smallspringboot.model.security.services.MyDatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
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

    @Autowired
    protected OAuth2UserService oAuth2UserService;

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
//                .antMatchers("/","/home","/registry","/static/**", "/error", "/authorisationMail", "/authorisationMail/*")
//                .anonymous()
//                .antMatchers(HttpMethod.POST,"/registry")
//                .anonymous()
//                лучше использовать permitAll, так как зарегистрированный пользователь не является анонимным, и не имеет доступа к страницам
//                указанным с разрешением  .anonymous()
                .antMatchers("/","/home","/registry","/static/**","/error", "/authorisationMail", "/authorisationMail/*")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/registry")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(new MyAuthenticationSuccessHandler(MAX_INACTIVE_INTERVAL))
                .failureUrl("/login?error=true")
                .permitAll().and()
                .logout()
                .permitAll().deleteCookies("JSESSIONID")
                .and()
                .oauth2Login(config->{
                    config.loginPage("/oauth2/authorization");
                    config.defaultSuccessUrl("/");
                    config.failureUrl("/login?error=true");
                    config.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService));
                })
                .httpBasic()
                .and()
                .sessionManagement() // реализация возможности захода только с одного адреса,для корректной работы нужно в пользователе
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
