package com.npn.spring.learning.spring.smallspringboot.model.security;

import java.util.Arrays;

/**
 * Перечень используемых ролей пользователя
 */
public enum UsersRoles {
    ROLE_USER,  ROLE_ADMIN;

    public static boolean containRole(String role){
        return Arrays.stream(UsersRoles.values()).anyMatch(x->x.toString().equals(role));
    }
}
