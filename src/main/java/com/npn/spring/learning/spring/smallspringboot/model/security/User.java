package com.npn.spring.learning.spring.smallspringboot.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.npn.spring.learning.spring.smallspringboot.model.reports.ReportTableCell;
import com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces.ReportTableRowRepresentation;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Класс определяющий пользователя
 */
@JsonIgnoreProperties(value = {"password"}, ignoreUnknown = true)
@Entity
@Table(name="usrs")
public class User implements UserDetails, ReportTableRowRepresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name="user_name",unique = true, nullable = false)
    private String name;

    @Column(name = "user_display_name")
    private String displayName;

    @Column(name = "pass_hash")
    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usrs_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name = "role_id", referencedColumnName = "id") )
    private final Set<MyUserAuthority> authorities = new CopyOnWriteArraySet();

    public User() {
    }

    public User(String name, String displayName, String password,
                boolean accountNonExpired, boolean accountNonLocked,
                boolean credentialsNonExpired, boolean enabled) {
        this.name = name.toLowerCase();
        this.displayName = displayName;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Set<MyUserAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return name;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getDisplayName() {
        return displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addAuthority(MyUserAuthority authority) {
        authorities.add(authority);
    }

    public void changeAuthorities (Collection<MyUserAuthority> collection) {
        authorities.clear();
        authorities.addAll(collection);
    }

    public boolean hasRole(UsersRoles role) {
        return authorities.stream().anyMatch(x->x.getRole().equals(role.name()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Возвращает класс в виде его представления для таблицы отчета
     *
     * @return Map<String, ReportTableCell> где, ключ - имя заголовка столбца таблицы, значение - объект {@link ReportTableCell}
     * определяющий представление поля в отчете
     */
    @Override
    public Map<String, ReportTableCell> getReportTableEntity() {
        Map<String,ReportTableCell> map = getDefaultReportTableEntity();
        map.put("Id", new ReportTableCell(ReportTableCell.ReportTableFieldType.NUMERIC,id.toString()));
        map.put("User name", new ReportTableCell(ReportTableCell.ReportTableFieldType.STRING,name));
        map.put("User display name", new ReportTableCell(ReportTableCell.ReportTableFieldType.STRING,displayName));
        map.put("accountNonExpired", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,String.valueOf(accountNonExpired)));
        map.put("accountNonLocked", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,String.valueOf(accountNonLocked)));
        map.put("credentialsNonExpired", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,String.valueOf(credentialsNonExpired)));
        map.put("enabled", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,String.valueOf(enabled)));
        map.put(MyUserAuthority.REPORT_COLUMN_REPRESENTATION,
                new ReportTableCell(
                        ReportTableCell.ReportTableFieldType.STRING,
                        authorities.stream().map(MyUserAuthority::getRole).reduce("",(a, b) -> a+ " " + b)));
        return map;
    }

    /**
     * Возвращает класс в виде его представления для таблицы отчета с пустыми значениями
     *
     * @return Map<String, ReportTableCell> где, ключ - имя заголовка столбца таблицы, значение - объект {@link ReportTableCell} (пустой)
     *  определяющий представление поля в отчете
     */
    public static Map<String, ReportTableCell> getDefaultReportTableEntity() {
        Map<String,ReportTableCell> map = new LinkedHashMap<>();
        map.put("Id", new ReportTableCell(ReportTableCell.ReportTableFieldType.NUMERIC,null));
        map.put("User name", new ReportTableCell(ReportTableCell.ReportTableFieldType.STRING,null));
        map.put("User display name", new ReportTableCell(ReportTableCell.ReportTableFieldType.STRING,null));
        map.put("accountNonExpired", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,null));
        map.put("accountNonLocked", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,null));
        map.put("credentialsNonExpired", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,null));
        map.put("enabled", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,null));
        map.put(MyUserAuthority.REPORT_COLUMN_REPRESENTATION,
                new ReportTableCell(
                        ReportTableCell.ReportTableFieldType.STRING,null));
        return map;
    }
}
