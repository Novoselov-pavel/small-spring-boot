package com.npn.spring.learning.spring.smallspringboot.model.html.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.HtmlNavElementsRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Служба, для работы с элементом меню
 */
@Service
public class HtmlNavElementService implements HtmlNavElementServiceInterface {


    private HtmlNavElementsRepository repository;

    /**
     * Получает HtmlNavElement элемент по Id
     * @param id id
     * @return HtmlNavElement or null
     */
    public HtmlNavElement loadById(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Получает HtmlNavElement элемент по name
     * @param name name
     * @return HtmlNavElement or null
     */
    public HtmlNavElement loadByName(String name) {
        if (name == null) throw new IllegalArgumentException("Null name in loadByName() is forbidden");
        return repository.findFirstByNameOrderByElementOrder(name.toLowerCase());
    }


    /**
     * Сохраняет элемент в БД
     * @param element HtmlNavElement
     */
    public void saveElement(HtmlNavElement element){
        if (element == null) throw new IllegalArgumentException("Null name in saveElement() is forbidden");
        repository.save(element);
    }


    /**
     * Возвращает список заголовков панели с учетом прав доступа пользователя
     *
     * @return List<HtmlNavElement>
     */
    public List<HtmlNavElement> getNavHeaderElements(User user) {
        List<HtmlNavElement> currentList = repository.findByParentIsNullOrderByElementOrder();
        return currentList
                .stream()
                .map(x->x.getElementForUserAuthority(user))
                .filter(x->x!=null)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список заголовков панели как строку c массивом объектов в формате Json, с учетом прав доступа пользователя
     *
     * @param user пользователь
     * @return Json строка
     */
    @Override
    public String getNavHeaderElementsAsJson(User user) throws JsonProcessingException {
        List<HtmlNavElement> currentList = getNavHeaderElements(user);
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(currentList);
        return value;
    }

    @Autowired
    public void setRepository(HtmlNavElementsRepository repository) {
        this.repository = repository;
    }
}
