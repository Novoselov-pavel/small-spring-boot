package com.npn.spring.learning.spring.smallspringboot.model.repositories;

import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * DAO для пользователей
 */
public interface UsersRepository extends CrudRepository<User,Long> {

    User findByName(String name);

    Optional<User> findById(Long id);
}
