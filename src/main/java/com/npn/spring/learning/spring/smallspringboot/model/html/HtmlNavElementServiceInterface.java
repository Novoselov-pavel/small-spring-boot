package com.npn.spring.learning.spring.smallspringboot.model.html;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;

import java.util.List;

/**
 * Интрефейс для работы с элементом меню
 */
public interface HtmlNavElementServiceInterface {
    /**
     * Получает HtmlNavElement элемент по Id
     * @param id id
     * @return HtmlNavElement or null
     */
    HtmlNavElement loadById(long id);

    /**
     * Получает HtmlNavElement элемент по name
     * @param name name
     * @return HtmlNavElement or null
     */
    HtmlNavElement loadByName(String name);

    /**
     * Сохраняет элемент в БД
     * @param element HtmlNavElement
     */
    void saveElement(HtmlNavElement element);
    /**
     * Возвращает список заголовков панели с учетом прав доступа пользователя
     *
     * @return List<HtmlNavElement>
     */
    List<HtmlNavElement> getNavHeaderElements(User user);

    /**
     * Возвращает список заголовков панели как строку c массивом объектов в формате Json, с учетом прав доступа пользователя
     *
     * @return Json строка
     * @throws JsonProcessingException при ошибке парсинга
     */
    String getNavHeaderElementsAsJson() throws JsonProcessingException;

    /**
     * Востанавливает Parent и сохраняет элементы в БД
     *
     * @param json строка в формате Json
     * @throws JsonProcessingException при ошибке парсинга
     */
    public void saveNavHeaderElementsFromJson(String json) throws JsonProcessingException;
}
