package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;

import java.util.List;

/**
 * Интерфейс по взаимодействию с ролями
 */
public interface UserAuthorityInterface {

    /**
     * Получает роль позльзователя по названию
     * @param name имя роли пользователя
     * @return MyUserAuthority или null, если она не найдена
     */
    MyUserAuthority getUserAuthorityByName(String name);

    /**
     * Возвращает все роли
     * @return List<MyUserAuthority>
     */
    List<MyUserAuthority> getAllAuthorities();

    /**
     * Возвращает все роли как объект Json
     * @return Json String
     */
    String getAllAuthoritiesAsJson() throws JsonProcessingException;
}
