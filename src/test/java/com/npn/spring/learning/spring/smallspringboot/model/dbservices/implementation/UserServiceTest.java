package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.npn.spring.learning.spring.smallspringboot.model.repositories.UsersRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceTest {

    @Mock
    UserService userService;

    @BeforeEach
    void init(@Mock UsersRepository usersRepository) {
        System.out.println("");
//        userService = new DefaultUserService(userRepository, settingRepository, mailClient);
//        lenient().when(settingRepository.getUserMinAge()).thenReturn(10);
//        when(settingRepository.getUserNameMinLength()).thenReturn(4);
//        lenient().when(userRepository.isUsernameAlreadyExists(any(String.class))).thenReturn(false);
//        this.settingRepository = settingRepository;
    }
    @Test
    void getAllUserAsJson() {
        System.out.println("");
    }

    @Test
    void saveUser() {
        System.out.println("");
    }
}

//package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;
//
//        import com.npn.spring.learning.spring.smallspringboot.model.repositories.UsersRepository;
//        import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
//        import com.npn.spring.learning.spring.smallspringboot.model.security.User;
//        import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
//        import org.junit.Before;
//        import org.junit.jupiter.api.BeforeEach;
//        import org.junit.jupiter.api.Test;
//        import org.junit.jupiter.api.extension.ExtendWith;
//        import org.junit.platform.runner.JUnitPlatform;
//        import org.junit.runner.RunWith;
//        import org.mockito.InjectMocks;
//        import org.mockito.Mock;
//        import org.mockito.Mockito;
//        import org.mockito.junit.jupiter.MockitoExtension;
//        import org.springframework.boot.test.context.SpringBootTest;
//
//        import java.util.ArrayList;
//        import java.util.List;
//        import java.util.Optional;
//
//        import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
//class UserServiceTest {
//
//
//    @Mock
//    private UsersRepository usersRepository;
//
//
//    private UserService userService = new UserService();
//
//    private List<User> list = new ArrayList<>();
//
//    @BeforeEach
//    public void init(){
////
////        usersRepository = Mockito.mock(UsersRepository.class);
//
//        MyUserAuthority userAuthority = new MyUserAuthority();
//        userAuthority.setRole(UsersRoles.USER_ROLE);
//        userAuthority.setId(1L);
//
//        MyUserAuthority adminAuthority = new MyUserAuthority();
//        adminAuthority.setRole(UsersRoles.ADMIN_ROLE);
//        adminAuthority.setId(2L);
//
//
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setPassword("password");
//        user1.setName("user1");
//        user1.setDisplayName("User1");
//        user1.setAccountNonExpired(true);
//        user1.setAccountNonLocked(true);
//        user1.setCredentialsNonExpired(true);
//        user1.setEnabled(true);
//        user1.addAuthority(userAuthority);
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setPassword("password2");
//        user2.setName("user2");
//        user2.setDisplayName("User2");
//        user2.setAccountNonExpired(true);
//        user2.setAccountNonLocked(true);
//        user2.setCredentialsNonExpired(true);
//        user2.setEnabled(true);
//        user2.addAuthority(userAuthority);
//        user2.addAuthority(adminAuthority);
//
//        list.add(user1);
//        list.add(user2);
//
//        Mockito.when(usersRepository.findAll()).thenReturn(list);
//        userService.setUsersRepository(usersRepository);
//
//    }
//
//    @Test
//    void findAll() {
//        List<User> userList = userService.findAll();
//        assertEquals(2,userList.size());
//        assertIterableEquals(list,userList);
//    }
//
//    @Test
//    void getAllUserAsJson() {
//    }
//}