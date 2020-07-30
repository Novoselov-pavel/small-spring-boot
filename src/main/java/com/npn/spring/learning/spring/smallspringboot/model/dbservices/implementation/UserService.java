package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.UsersRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Служба по взаимодействию с пользователем
 */
@Service("userService")
public class UserService implements UserServiceInterface {

    private UserAuthorityInterface userAuthority;

    private UsersRepository usersRepository;

    private PasswordEncoder passwordEncoder;

    /**
     * Возвращает пользователя, если он есть в базе данных, или null
     * @param name имя пользователя
     * @return User или null
     */
    @Override
    public User findByName(String name) {
        return usersRepository.findByName(name.toLowerCase());
    }

    /**
     * Создает, сохраняет в базу данных и возвращает нового пользователя с ролью USER_ROLE
     *
     * @param name
     * @param password
     * @return
     */
    @Override
    public User addNewUser(String name, String password) throws UserAlreadyExist {
        User user = findByName(name);
        if (user!=null) throw new UserAlreadyExist();
        String encodedPassword = passwordEncoder.encode(password);
        MyUserAuthority authority = userAuthority.getUserAuthorityByName(UsersRoles.USER_ROLE.toString());

        User newUser = new User(name,name,encodedPassword,
                true,true,true,true);
        newUser.addAuthority(authority);

        return usersRepository.save(newUser);
    }

    /**
     * Запрашивает из базы данных всех пользователей
     *
     * @return List<User>
     */
    @Override
    public List<User> findAll() {
        return StreamSupport
                .stream(usersRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает List всех пользователей со стертыми паролями в виде Json String
     *
     * @return Json String
     */
    @Override
    public String getAllUserAsJson() throws JsonProcessingException {
        List<User> users = findAll();
        String result = new ObjectMapper().writeValueAsString(users);
        //TODO
        return result;
    }

    /**
     * Сохраняет в БД измененные данные пользователя (кроме пароля)
     *
     * @param Json String
     */
    @Override
    public void saveUser(String Json) {
        //TODO
    }

    @Autowired
    public void setUserAuthority(UserAuthorityInterface userAuthority) {
        this.userAuthority = userAuthority;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
