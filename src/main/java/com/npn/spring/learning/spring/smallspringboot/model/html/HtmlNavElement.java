package com.npn.spring.learning.spring.smallspringboot.model.html;

import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "header_items_authority",
            joinColumns = @JoinColumn(name = "header_item_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name = "role_id", referencedColumnName = "id") )
    private final Set<MyUserAuthority> authorities = new CopyOnWriteArraySet();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HtmlNavElement parent;

    @OneToMany(mappedBy = "parent")
    private final Set<HtmlNavElement> children = new CopyOnWriteArraySet<>();

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

    public Set<HtmlNavElement> getChildren() {
        return children.stream().collect(Collectors.toUnmodifiableSet());
    }

    public void setParent(HtmlNavElement parent) {
        this.parent = parent;
    }

    public void setAuthority(MyUserAuthority userAuthority) {
        authorities.add(userAuthority);
    }

    public void setChildren(HtmlNavElement element) {
        children.add(element);
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
