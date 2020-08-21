package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;
import org.json.simple.parser.ParseException;

import java.util.List;

/**
 * Интерфейс по взаимодействию с пользователем
 */
public interface UserServiceInterface {
    /**
     * Возвращает пользователя, если он есть в базе данных, или null
     * @param id id пользователя
     * @return User или null
     */
    User findById(Long id);

    /**
     * Возвращает пользователя, если он есть в базе данных, или null
     * @param name имя пользователя
     * @return User или null
     */
    User findByName(String name);

    /**
     * Обновляет данные пользователя
     *
     * @param id пользователя
     * @param json String
     * @return нового пользователя, или null, если он не был найден
     * @throws ParseException при ошибке распознования Json
     */
    User updateUser(Long id, String json) throws ParseException;

    /**
     * Добавляет нового пользователя
     *
     * @param name имя пользователя
     * @param password пароль
     * @return добавленного пользователя
     * @throws UserAlreadyExist если пользователь уже существует
     */
    User addNewUser(String name, String password) throws UserAlreadyExist;

    /**
     * Запрашивает из базы данных всех пользователей
     *
     * @return List<User>
     */
    List<User> findAll();

    /**
     * Возвращает List всех пользователей со стертыми паролями в виде Json String
     *
     * @return Json String
     */
    String getAllUserAsJson() throws JsonProcessingException;

    /**
     * Удаляет пользователя
     *
     * @param id id пользователя
     */
    void deleteUser(Long id);

    /**
     * Возвращает всех пользователей, аккаунты которых не активированы
     * @param list список уже отосланных писем
     * @return список пользователей
     */
    List<User> getAllNonConfirmedUser(List<AuthorisationMailData> list);

    /**
     * Сохраняет пользователя
     * @param user пользователь
     * @return сохраненный пользователь
     */
    User saveUser(User user);
}
