package com.npn.spring.learning.spring.smallspringboot.model.repositories;

import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DAO для данных письмо авторизации
 */
public interface AuthorisationMailDataRepository extends JpaRepository<AuthorisationMailData, Long> {

    Optional<AuthorisationMailData> findFirstByUserId(Long userId);
}
