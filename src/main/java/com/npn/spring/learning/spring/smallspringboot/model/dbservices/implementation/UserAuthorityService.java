package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.UserAuthoritiesRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Служба для работы с ролями
 */
@Service
public class UserAuthorityService implements UserAuthorityInterface {

    private UserAuthoritiesRepository userAuthoritiesRepository;

    /**
     * Получает
     * @param name
     * @return
     */
    @Override
    public MyUserAuthority getUserAuthorityByName(String name) {
        return userAuthoritiesRepository.findByRole(name.toUpperCase());
    }


    /**
     * Возвращает все роли
     * @return
     */
    public List<MyUserAuthority> getAllAuthorities() {
        return StreamSupport
                .stream(userAuthoritiesRepository.findAll().spliterator(),false)
                .sorted(Comparator.comparingLong(MyUserAuthority::getId))
                .collect(Collectors.toList());
    }
    @Autowired
    public void setUserAuthoritiesRepository(UserAuthoritiesRepository userAuthoritiesRepository) {
        this.userAuthoritiesRepository = userAuthoritiesRepository;
    }
}
