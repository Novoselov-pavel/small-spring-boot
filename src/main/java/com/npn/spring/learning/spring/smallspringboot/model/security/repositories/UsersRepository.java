package com.npn.spring.learning.spring.smallspringboot.model.security.repositories;

import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User,Long> {

    User findByName(String name);
}
