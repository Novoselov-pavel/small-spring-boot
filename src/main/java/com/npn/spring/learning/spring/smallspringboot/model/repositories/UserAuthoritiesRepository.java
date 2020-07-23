package com.npn.spring.learning.spring.smallspringboot.model.repositories;

import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import org.springframework.data.repository.CrudRepository;

public interface UserAuthoritiesRepository extends CrudRepository<MyUserAuthority, Long> {
    MyUserAuthority findByRole(String role);
}
