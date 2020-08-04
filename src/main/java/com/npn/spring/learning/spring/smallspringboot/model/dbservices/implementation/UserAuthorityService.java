package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * Получает роль позльзователя по названию
     * @param name имя роли пользователя
     * @return MyUserAuthority или null, если она не найдена
     */
    @Override
    public MyUserAuthority getUserAuthorityByName(String name) {
        return userAuthoritiesRepository.findByRole(name.toUpperCase());
    }


    /**
     * Возвращает все роли
     * @return List<MyUserAuthority>
     */
    public List<MyUserAuthority> getAllAuthorities() {
        return StreamSupport
                .stream(userAuthoritiesRepository.findAll().spliterator(),false)
                .sorted(Comparator.comparingLong(MyUserAuthority::getId))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает все роли как объект Json
     *
     * @return Json String
     */
    @Override
    public String getAllAuthoritiesAsJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getAllAuthorities());
    }

    @Autowired
    public void setUserAuthoritiesRepository(UserAuthoritiesRepository userAuthoritiesRepository) {
        this.userAuthoritiesRepository = userAuthoritiesRepository;
    }
}
