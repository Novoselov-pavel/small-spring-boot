package com.npn.spring.learning.spring.smallspringboot.model.security.servises;

import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyDatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

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
        User user= usersRepository.findByName(username.toLowerCase());
        if (user == null) throw new UsernameNotFoundException("User "+username+" not found");
        return user;
    }
}
