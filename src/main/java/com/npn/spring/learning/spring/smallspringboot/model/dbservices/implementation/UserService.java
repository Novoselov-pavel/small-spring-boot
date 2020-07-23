package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

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

/**
 * Служба по взаимодействию с пользователем
 */
@Service("userService")
public class UserService implements UserServiceInterface {

    @Autowired
    private UserAuthorityInterface userAuthority;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
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
        User user = findByName(name.toLowerCase());
        if (user!=null) throw new UserAlreadyExist();
        String encodedPassword = passwordEncoder.encode(password);
        MyUserAuthority authority = userAuthority.getUserAuthorityByName(UsersRoles.USER_ROLE.toString());

        User newUser = new User(name.toLowerCase(),name,encodedPassword,
                true,true,true,true);
        newUser.addAuthority(authority);

        return usersRepository.save(newUser);
    }

}
