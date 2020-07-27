package com.npn.spring.learning.spring.smallspringboot.model.security.servises;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation.UserService;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Реализация UserDetailsService для авторизации пользователей
 */
public class MyDatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    /**
     * Поиск пользователя по имени пользователя, регистр игнорируется (имя приводится к нижнему регистру).
     *
     * @param username имя пользователя
     * @return объект User (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userService.findByName(username);
        if (user == null) throw new UsernameNotFoundException("User "+username+" not found");
        return user;
    }
}
