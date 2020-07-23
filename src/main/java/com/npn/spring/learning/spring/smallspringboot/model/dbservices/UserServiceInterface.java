package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;

/**
 * Интерфейс по взаимодействию с пользователем
 */
public interface UserServiceInterface {

    /**
     * Возвращает пользователя, если он есть в базе данных, или null
     * @param name имя пользователя
     * @return User или null
     */
    public User findByName(String name);

    public User addNewUser(String name, String password) throws UserAlreadyExist;

}
