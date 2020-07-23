package com.npn.spring.learning.spring.smallspringboot.model.security.servises;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс для установки срока жизни сессии пользователя
 */
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private int maxInactiveInterval;

    public MyAuthenticationSuccessHandler(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /**
     * Устанавливает срок жизни сессии пользователя и перенаправляет на страницу, с которой пришел запрос об авторизации
     *
     * @param httpServletRequest HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @param authentication Authentication
     * @throws IOException при ошибке
     * @throws ServletException при ошибке
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getSession(false).setMaxInactiveInterval(maxInactiveInterval);
        super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
    }
}
