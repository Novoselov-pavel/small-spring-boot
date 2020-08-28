package com.npn.spring.learning.spring.smallspringboot.model.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyOAuth2User implements OAuth2User, OidcUser {
    private OidcUser oidcUser;
    private final List<GrantedAuthority> authorities = new CopyOnWriteArrayList<>();
    private Map<String, Object> attributes;
    private String name;
    private String password;
    private String email;
    private String displayName;
    private User refUser;

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        return attributes;
    }

    public MyOAuth2User(OidcUser user, User dbUser) {
        this(user.getName(),
                user.getFullName(),
                user.getEmail(),
                user.getAccessTokenHash(),
                user.getAttributes(),
                user.getAuthorities(),
                dbUser,
                user.getIdToken(),
                user.getUserInfo());
    }

    public MyOAuth2User(String name,
                        String displayName,
                        String email,
                        String password,
                        Map<String, Object> attributes,
                        Collection<? extends GrantedAuthority> authorities,
                        User refUser, OidcIdToken token, OidcUserInfo userInfo) {
        Assert.notNull(refUser,"MyOAuth2User user cannot be null");
        this.name = name;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
        this.refUser = refUser;
        this.attributes = Collections.unmodifiableMap(attributes);
        this.authorities.addAll(authorities);
        this.oidcUser = new DefaultOidcUser(getAuthorities(),token, userInfo);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (refUser!=null) {
            List<GrantedAuthority> list = new ArrayList<>(authorities);
            list.addAll(refUser.getAuthorities());
            return Collections.unmodifiableList(list);
        }
        return Collections.unmodifiableList(authorities);
    }

    /**
     * Returns the name of the authenticated <code>Principal</code>. Never <code>null</code>.
     *
     * @return the name of the authenticated <code>Principal</code>
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }


    public String getDisplayName() {
        return displayName;
    }


    public User getRefUser() {
        return refUser;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyOAuth2User that = (MyOAuth2User) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(refUser, that.refUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, refUser);
    }

    /**
     * Returns the claims about the user.
     * The claims are aggregated from {@link #getIdToken()} and {@link #getUserInfo()} (if available).
     *
     * @return a {@code Map} of claims about the user
     */
    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    /**
     * Returns the {@link OidcUserInfo UserInfo} containing claims about the user.
     *
     * @return the {@link OidcUserInfo} containing claims about the user.
     */
    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    /**
     * Returns the {@link OidcIdToken ID Token} containing claims about the user.
     *
     * @return the {@link OidcIdToken} containing claims about the user.
     */
    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }
}
