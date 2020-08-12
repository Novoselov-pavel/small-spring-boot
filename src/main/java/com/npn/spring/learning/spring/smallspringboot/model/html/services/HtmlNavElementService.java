package com.npn.spring.learning.spring.smallspringboot.model.html.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.HtmlNavElementsRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
     * @return Json строка
     */
    @Override
    public String getNavHeaderElementsAsJson() throws JsonProcessingException {
        List<HtmlNavElement> currentList = repository.findByParentIsNullOrderByElementOrder();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(currentList);
        return value;
    }

    /**
     * Востанавливает Parent и сохраняет элементы в БД
     *
     * @param json строка в формате Json
     * @throws JsonProcessingException при ошибке парсинга
     */
    @Override
    public void saveNavHeaderElementFromJson(String json) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        HtmlNavElement element = mapper.readValue(json, HtmlNavElement.class);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        String parentIdString = jsonObject.get("parentId").toString();
        HtmlNavElement parent = null;
        if (!parentIdString.equals("#")) {
            parent = loadById(Long.valueOf(parentIdString));
        }
        element.setParent(parent);
//
//        if (element.getId()!=null){
//            element.setChildren(loadById(element.getId()).getChildren());
//        }
        saveElement(element);
    }

    /**
     * Удаляет из базы данных элемент меню
     *
     * @param id удаляемого элемента
     */
    @Override
    public void deleteNavHeaderElement(Long id) {
        repository.deleteById(id);
    }

    @Autowired
    public void setRepository(HtmlNavElementsRepository repository) {
        this.repository = repository;
    }


}
