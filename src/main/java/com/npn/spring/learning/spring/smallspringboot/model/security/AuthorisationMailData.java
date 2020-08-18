package com.npn.spring.learning.spring.smallspringboot.model.security;


import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

/**
 * Данные письма авторизации
 */
@Entity
@Table(name = "authorisation_mail")
public class AuthorisationMailData {

    @Transient
    @Value("${authorisation.mail.link.template}")
    private String LINK_TEMPLATE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name_token", nullable = false)
    private String userNameToken;

    @Column(name = "authorisation_token", nullable = false)
    private String authorisationToken;

    @Column(name = "expired_date", nullable = false)
    private Long expiredDate;

    @Column(name = "is_sended", nullable = false)
    private boolean isSended = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNameToken() {
        return userNameToken;
    }

    public void setUserNameToken(String userNameToken) {
        this.userNameToken = userNameToken;
    }

    public String getAuthorisationToken() {
        return authorisationToken;
    }

    public void setAuthorisationToken(String authorisationToken) {
        this.authorisationToken = authorisationToken;
    }

    public Long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public boolean isSended() {
        return isSended;
    }

    public void setSended(boolean sended) {
        isSended = sended;
    }

    public String getLink() {
        return String.format(LINK_TEMPLATE,getUserNameToken(),getAuthorisationToken());
    }
}
