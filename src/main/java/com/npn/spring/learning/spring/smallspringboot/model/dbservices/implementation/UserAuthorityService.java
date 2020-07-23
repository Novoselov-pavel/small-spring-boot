package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.UserAuthoritiesRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityService implements UserAuthorityInterface {

    private UserAuthoritiesRepository userAuthoritiesRepository;

    @Override
    public MyUserAuthority getUserAuthorityByName(String name) {
        return userAuthoritiesRepository.findByRole(name.toUpperCase());
    }

    @Autowired
    public void setUserAuthoritiesRepository(UserAuthoritiesRepository userAuthoritiesRepository) {
        this.userAuthoritiesRepository = userAuthoritiesRepository;
    }
}
