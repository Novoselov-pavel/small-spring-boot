package com.npn.spring.learning.spring.smallspringboot.model.html;

import com.npn.spring.learning.spring.smallspringboot.model.html.services.HtmlNavElementService;

import java.util.List;

/**
 * Класс-шаблон, для включения в model, для стандартной страницы
 */
public class HtmlThymeleafPage {
    /**
     * Имя данного объекта для включения в Model
     */
    private static final String thymeleafObjectName = "htmlThymeleafPage";
    /**
     * Заголовок странцы
     */
    private String title;

    /**
     * Ссылка на фрагмент для шаблона BlankPage, фрагмент должен быть размещен в BlankPageFragments.html
     */
    private String fragmentName;

    /**
     * Скрывать сообщение в шаблоне
     */
    private boolean hideMessage = true;
    /**
     * Текст сообщения в шаблоне
     */
    private String message;

    /**
     * Ссылка на фрагмент верхнего меню, для templates/views/StandardPageTemplate.html, шаблон меню должен быть размещен
     * в NavbarFragments.html
     */
    private String navName;

    /**
     * Элеметны меню, согласно которым отрисовывается верхнее меню.
     * см. классы {@link HtmlNavElement}, {@link HtmlNavElementService}
     */
    private List<HtmlNavElement> navElements;

    /**
     * Ссылка на тело страницы для шаблона templates/views/StandardPageTemplate.html.
     * Формат "views/fragments/AdminBodyFragment.html :: body"
     */
    private String bodyFragmentRef;

    /**
     * Возваращает имя данного объекта для включения в Model
     * @return имя данного объекта для включения в Model
     */
    public static String getThymeleafObjectName() {
        return thymeleafObjectName;
    }

    /**
     * Сообщает, требуется ли скрывать сообщение
     * @return true  если сообщение должно быть скрыто, иначе false
     */
    public boolean isHideMessage() {
        return hideMessage;
    }

    /**
     * Устанавливает, требуется ли скрывать сообщение
     * @param hideMessage true если сообщение должно быть скрыто, иначе false
     */
    public void setHideMessage(boolean hideMessage) {
        this.hideMessage = hideMessage;
    }

    /**
     * Возвращает сообщение
     * @return сообщение
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает сообщение
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Возвращает имя фрагмента для шаблона BlankPage, фрагмент должен быть размещен в BlankPageFragments.html
     *
     * @return имя фрагмента
     */
    public String getFragmentName() {
        return fragmentName;
    }

    /**
     * Устанавливает имя фрагмента для шаблона BlankPage, фрагмент должен быть размещен в BlankPageFragments.html
     *
     * @param fragmentName имя фрагмента
     */
    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    /**
     * Получает заголовок странцы
     * @return заголовок страницы
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок странцы
     * @param title заголовок страницы
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает ссылку на фрагмент верхнего меню, для templates/views/StandardPageTemplate.html, шаблон меню должен быть размещен
     * в NavbarFragments.html
     * @return ссылка на фрагмент верхнего меню
     */
    public String getNavName() {
        return navName;
    }

    /**
     * Устаналивает ссылку на фрагмент верхнего меню, для templates/views/StandardPageTemplate.html, шаблон меню должен быть размещен
     * в NavbarFragments.html
     * @param navName ссылка на фрагмент верхнего меню
     */
    public void setNavName(String navName) {
        this.navName = navName;
    }

    /**
     * Возвращает список элеметов меню, согласно которым отрисовывается верхнее меню.
     * см. классы {@link HtmlNavElement}, {@link HtmlNavElementService}
     * @return список элеметов меню
     */
    public List<HtmlNavElement> getNavElements() {
        return navElements;
    }

    /**
     * Устанавливает список элеметов меню, согласно которым отрисовывается верхнее меню.
     * см. классы {@link HtmlNavElement}, {@link HtmlNavElementService}
     *
     * @param navElements список элеметов меню
     */
    public void setNavElements(List<HtmlNavElement> navElements) {
        this.navElements = navElements;
    }

    /**
     * Возвращает ссылку на тело страницы для шаблона templates/views/StandardPageTemplate.html.
     * Формат "views/fragments/AdminBodyFragment.html :: body"
     *
     * @return ссылка на тело страницы для шаблона StandardPageTemplate.html
     */
    public String getBodyFragmentRef() {
        return bodyFragmentRef;
    }

    /**
     * Устанавливает ссылку на тело страницы для шаблона templates/views/StandardPageTemplate.html.
     * Формат "views/fragments/AdminBodyFragment.html :: body"
     *
     * @param bodyFragmentRef ссылка на тело страницы для шаблона StandardPageTemplate.html
     */
    public void setBodyFragmentRef(String bodyFragmentRef) {
        this.bodyFragmentRef = bodyFragmentRef;
    }
}
