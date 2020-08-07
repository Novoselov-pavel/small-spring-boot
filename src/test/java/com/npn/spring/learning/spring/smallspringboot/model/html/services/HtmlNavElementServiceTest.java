package com.npn.spring.learning.spring.smallspringboot.model.html.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation.UserService;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementType;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.HtmlNavElementsRepository;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.UsersRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class HtmlNavElementServiceTest {

    @Mock
    private HtmlNavElementsRepository repository;

    @InjectMocks
    private HtmlNavElementService service;

    private MyUserAuthority user = new MyUserAuthority();

    private MyUserAuthority admin = new MyUserAuthority();
    List<HtmlNavElement> list = new ArrayList<>();

    @BeforeEach
    void init() {

        user.setRole(UsersRoles.ROLE_USER);
        user.setId(1L);


        admin.setRole(UsersRoles.ROLE_ADMIN);
        admin.setId(2L);

        HtmlNavElement rootElement = new HtmlNavElement();
        rootElement.setElementOrder(0);
        rootElement.setName("rootElement");
        rootElement.setParent(null);
        rootElement.setDescription("Меню 1");
        rootElement.setId(1L);
        rootElement.setType(HtmlNavElementType.MENU);
        rootElement.setHref(null);
        rootElement.setAuthority(user);
        rootElement.setAuthority(admin);

        HtmlNavElement child1 = new HtmlNavElement();
        child1.setElementOrder(0);
        child1.setName("child1");
        child1.setParent(rootElement);
        child1.setDescription("Объект меню 1");
        child1.setId(2L);
        child1.setType(HtmlNavElementType.ELEMENT);
        child1.setHref("/a");
        child1.setAuthority(user);
        child1.setAuthority(admin);

        HtmlNavElement child2 = new HtmlNavElement();
        child2.setElementOrder(2);
        child2.setName("child2");
        child2.setParent(rootElement);
        child2.setDescription("Объект меню 2");
        child2.setId(4L);
        child2.setType(HtmlNavElementType.ELEMENT);
        child2.setHref("/b");
        child2.setAuthority(user);
        child2.setAuthority(admin);

        HtmlNavElement child3 = new HtmlNavElement();
        child3.setElementOrder(1);
        child3.setName("child3");
        child3.setParent(rootElement);
        child3.setDescription("Объект меню 2");
        child3.setId(3L);
        child3.setType(HtmlNavElementType.DIVIDER);
        child3.setHref("/b");
        child3.setAuthority(user);
        child3.setAuthority(admin);

        rootElement.setChild(child1);
        rootElement.setChild(child3);
        rootElement.setChild(child2);

        service.setRepository(repository);

        list.add(rootElement);

        Mockito.when(repository.findByParentIsNullOrderByElementOrder()).thenReturn(list);
    }


    @Test
    void getNavHeaderElementsAsJson() {
        try {
            String s = service.getNavHeaderElementsAsJson();
            ObjectMapper mapper = new ObjectMapper();
            List<HtmlNavElement> readList = mapper.readValue(s, new TypeReference<List<HtmlNavElement>>(){});
            assertEquals(list.size(),readList.size());
            assertEquals(list,readList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }

    }
}