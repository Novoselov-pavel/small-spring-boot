package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;

/**
 * Интерфейс по взаимодействию с ролями
 */
public interface UserAuthorityInterface {

    public MyUserAuthority getUserAuthorityByName(String name);
}
