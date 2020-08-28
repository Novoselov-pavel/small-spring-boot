package com.npn.spring.learning.spring.smallspringboot.model.security.servises;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyOAuth2User;
import com.npn.spring.learning.spring.smallspringboot.model.security.MyUserAuthority;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("myOAuth2UserService")
public class MyOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    UserServiceInterface userService;

    @Autowired
    UserAuthorityInterface authorityService;
    /**
     * Returns an {@link OAuth2User} after obtaining the user attributes of the End-User from the UserInfo Endpoint.
     *
     * @param userRequest the user request
     * @return an {@link OAuth2User}
     * @throws OAuth2AuthenticationException if an error occurs while attempting to obtain the user attributes from the UserInfo Endpoint
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);
        MyOAuth2User oAuth2User = new MyOAuth2User(oidcUser,getUser(oidcUser));
        return oAuth2User;
    }

    private User getUser(OidcUser user) {
        User retVal = userService.findByName(user.getName());

        if (retVal!=null) {
            return retVal;
        }

        MyUserAuthority authority = authorityService.getUserAuthorityByName(UsersRoles.ROLE_USER.toString());
        retVal = new User(user.getName(),
                user.getFullName(),
                user.getAccessTokenHash(),
                true,
                true,
                true,
                true);
        retVal.addAuthority(authority);

        return userService.saveUser(retVal);


    }


}
