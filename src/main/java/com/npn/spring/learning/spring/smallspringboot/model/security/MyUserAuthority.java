package com.npn.spring.learning.spring.smallspringboot.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.npn.spring.learning.spring.smallspringboot.model.reports.ReportTableCell;
import com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces.ReportTableRowRepresentation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Класс определяющий роли пользователя
 */
@Entity
@Table(name = "users_roles")
public class MyUserAuthority implements GrantedAuthority {
    @JsonIgnore
    @Transient
    public static final String REPORT_COLUMN_REPRESENTATION = "Roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private long id;

    @Column(name="role_name")
    private String role;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usrs_authority",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name = "user_id", referencedColumnName = "id") )
    private final Set<User> authorities = new CopyOnWriteArraySet();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toUpperCase();
    }

    public void setRole(UsersRoles role) {
        this.role = role.name();
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUserAuthority that = (MyUserAuthority) o;
        return id == that.id &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
