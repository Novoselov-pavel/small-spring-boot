package com.npn.spring.learning.spring.smallspringboot.model.html;

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
    public HtmlNavElement loadById(long id);

    /**
     * Получает HtmlNavElement элемент по name
     * @param name name
     * @return HtmlNavElement or null
     */
    public HtmlNavElement loadByName(String name);

    /**
     * Сохраняет элемент в БД
     * @param element HtmlNavElement
     */
    public void saveElement(HtmlNavElement element);
    /**
     * Возвращает список заголовков панели с учетом прав доступа пользователя
     *
     * @return List<HtmlNavElement>
     */
    public List<HtmlNavElement> getNavHeaderElements(User user);
}
