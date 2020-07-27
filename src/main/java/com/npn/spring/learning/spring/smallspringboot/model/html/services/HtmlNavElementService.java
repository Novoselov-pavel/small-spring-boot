package com.npn.spring.learning.spring.smallspringboot.model.html.services;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.HtmlNavElementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Служба, для работы с элементом меню
 */
@Service
public class HtmlNavElementService {

    @Autowired
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
        return repository.findFirstByName(name.toLowerCase());
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
     * Возвращает список заголовков панели
     *
     * @return List<HtmlNavElement>
     */
    public List<HtmlNavElement> getNavHeaderElements() {
        return repository.findByParentIsNull();
    }


}
