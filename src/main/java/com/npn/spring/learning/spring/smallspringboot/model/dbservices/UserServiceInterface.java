package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;

import java.util.List;

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

    /**
     * Запрашивает из базы данных всех пользователей
     *
     * @return List<User>
     */
    public List<User> findAll();

    /**
     * Возвращает List всех пользователей со стертыми паролями в виде Json String
     *
     * @return Json String
     */
    public String getAllUserAsJson() throws JsonProcessingException;

    /**
     * Сохраняет в БД измененные данные пользователя (кроме пароля)
     *
     * @param Json String
     */
    public void saveUser(String Json);
}
