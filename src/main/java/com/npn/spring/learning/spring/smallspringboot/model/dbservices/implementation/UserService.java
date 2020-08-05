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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
     * Обновляет данные пользователя
     *
     * @param id пользователя
     * @return нового пользователя, или null, если он не был найден
     * @throws ParseException при ошибке распознования Json
     */
    @Override
    public User updateUser(final Long id, final String json) throws ParseException {
        Optional<User> userObject = usersRepository.findById(id);
        if (!userObject.isPresent()) return null;

        User user = userObject.get();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        user = fillUserFromJSONObject(user, jsonObject);
        user = usersRepository.save(user);
        return user;
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
        MyUserAuthority authority = userAuthority.getUserAuthorityByName(UsersRoles.ROLE_USER.toString());

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
        return new ObjectMapper().writeValueAsString(users);
    }

    /**
     * Удаляет пользователя
     *
     * @param id id пользователя
     */
    @Override
    public void deleteUser(Long id) {
        Optional<User> user = usersRepository.findById(id);
        if (user.isPresent()) {
            usersRepository.delete(user.get());
        }
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

    /**
     * Заполняет переданного пользователя данными из JSONObject
     *
     * @param user переданный User
     * @param object JSONObject
     * @return переданный User
     */
    private User fillUserFromJSONObject(User user, JSONObject object) {
        user.setName(object.get("name").toString());
        user.setDisplayName(object.get("displayName").toString());
        user.setEnabled((Boolean) object.get("enabled"));
        user.setAccountNonExpired((Boolean) object.get("accountNonExpired"));
        user.setAccountNonLocked((Boolean) object.get("accountNonLocked"));
        user.setCredentialsNonExpired((Boolean) object.get("credentialsNonExpired"));
        JSONArray roles = (JSONArray) object.get("roles");
        List<MyUserAuthority> authorities= (List<MyUserAuthority>) roles
                .stream()
                .map(x-> userAuthority.getUserAuthorityByName(x.toString())).collect(Collectors.toList());
        user.changeAuthorities(authorities);
        return user;
    }
}
