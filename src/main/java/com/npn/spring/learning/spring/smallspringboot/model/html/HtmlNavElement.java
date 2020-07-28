package com.npn.spring.learning.spring.smallspringboot.model.html;

import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Класс описывающий элемент навигационного меню
 */
@Entity
@Table(name = "header_items")
public class HtmlNavElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "item_name", unique = true, nullable = false)
    private String name;

    @Column(name = "item_type", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "href")
    private String href;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "header_items_authority",
            joinColumns = @JoinColumn(name = "header_item_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name = "role_id", referencedColumnName = "id") )
    private final Set<MyUserAuthority> authorities = new CopyOnWriteArraySet();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HtmlNavElement parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @OrderBy("elementOrder")
    private final Set<HtmlNavElement> children = new CopyOnWriteArraySet<>();

    @Column(name = "items_order")
    private int elementOrder;

    /**
     * Возвращает элемент меню и его подэлементы, который доступен для пользователя.
     * Создает копию исходного элемента.
     *
     * @param user пользовател
     * @return копия HtmlNavElement или Null
     */
    public HtmlNavElement getElementForUserAuthority(final User user) {
        return getElementForUserAuthority(user.getAuthorities());
    }

    /**
     * Возвращает элемент меню и его подэлементы, который доступен для указанных ролей.
     * Создает копия исходного элемента
     *
     * @param authorities роли пользователя
     * @return копию HtmlNavElement или Null
     */
    public HtmlNavElement getElementForUserAuthority(final Set<MyUserAuthority> authorities) {
        return cloneElementForUserAuthority(this,authorities);
    }

    /**
     * Клонирует HtmlNavElement
     *
     * @param element
     * @return
     */
    public HtmlNavElement cloneElementForUserAuthority(final HtmlNavElement element,
                                                       final Set<MyUserAuthority> authorities) {
        if (!isElementVisible(element,authorities)) return null;

        HtmlNavElement retVal = new HtmlNavElement();
        retVal.setId(element.getId());
        retVal.setName(element.getName());
        retVal.setType(element.getType());
        retVal.setDescription(element.getDescription());
        retVal.setHref(element.getHref());
        retVal.setAuthorities(element.getAuthorities());
        retVal.setParent(element.getParent());
        retVal.setElementOrder(element.getElementOrder());
        List<HtmlNavElement> children = element.getChildren()
                .stream()
                .filter(x->isElementVisible(x,authorities))
                .map(x->cloneElementForUserAuthority(x,authorities))
                .sorted(Comparator.comparingInt(HtmlNavElement::getElementOrder))
                .collect(Collectors.toList());
        retVal.setChildren(children);
        return retVal;
    }

    /**
     * Проверяет, выводится ли на экран элемент меню для данного пользователя
     *
     * @param element
     * @param authorities
     * @return
     */
    private boolean isElementVisible(final HtmlNavElement element,
                                     final Set<MyUserAuthority> authorities){
        return element.getAuthorities().stream().map(x->authorities.contains(x)).filter(x->x).count()>0;
    }

    public Long getId() {
        return id;
    }

    /**
     * Изменение id элемента
     *
     * @param id id элемента
     * @deprecated рекомендуется не менять id элемента
     */
    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getType() {
        return type;
    }

    /**
     * Изменение типа элемента меню
     * @param type
     * @deprecated Рекомендуется использовать метод setType(HtmlNavElementType type)
     */
    @Deprecated
    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public void setType(HtmlNavElementType type) {
        this.type = type.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MyUserAuthority> getAuthorities() {
        return authorities.stream().collect(Collectors.toUnmodifiableSet());
    }

    public HtmlNavElement getParent() {
        return parent;
    }

    public List<HtmlNavElement> getChildren() {
        return children.stream().collect(Collectors.toUnmodifiableList());
    }

    public void setParent(HtmlNavElement parent) {
        this.parent = parent;
    }

    public void setAuthority(MyUserAuthority userAuthority) {
        authorities.add(userAuthority);
    }

    public void setAuthorities (Collection<MyUserAuthority> userAuthorities) {
        authorities.addAll(userAuthorities);
    }

    public void setChild(HtmlNavElement element) {
        children.add(element);
    }

    public void setChildren(Collection<HtmlNavElement> collection) {
        children.addAll(collection);
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getElementOrder() {
        return elementOrder;
    }

    public void setElementOrder(int elementOrder) {
        this.elementOrder = elementOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlNavElement that = (HtmlNavElement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
