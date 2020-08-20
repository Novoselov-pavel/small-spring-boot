package com.npn.spring.learning.spring.smallspringboot.model.dbservices;

import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.AuthorisationEmailExpired;

import java.util.List;

/**
 * Интрефейс для работы с данными письма авторизации
 */
public interface AuthorisationMailDataInterface {

    /**
     * Получает AuthorisationMailData из базы данных по его ID
     *
     * @param id id письма
     * @return AuthorisationMailData или null
     */
    AuthorisationMailData getAuthorisationMailDataById(final Long id);

    /**
     * Получает AuthorisationMailData из базы данных по ID пользователя
     *
     * @param userId id пользователя
     * @return AuthorisationMailData или null
     */
    AuthorisationMailData getAuthorisationMailDataByUserId(final Long userId);

    /**
     * Сохраняет AuthorisationMailData в БД.
     * @param data AuthorisationMailData
     */
    void save(final AuthorisationMailData data);

    /**
     * Подтверждает авторизацию пользователя
     *
     * @param userNameToken token for name of user
     * @param authorisationToken password token
     */
    boolean confirmAuthorisation(final String userNameToken, final String authorisationToken) throws AuthorisationEmailExpired;

    /**
     * Получает AuthorisationMailData по userNameToken
     * @param userNameToken token for name of user
     * @return AuthorisationMailData или null
     */
    AuthorisationMailData getAuthorisationMailDataByUserNameToken(final String userNameToken);

    /**
     * Получает список всех AuthorisationMailData
     *
     * @return List<AuthorisationMailData>
     */
    List<AuthorisationMailData> getAllAuthorisationMailData();

    /**
     * Удаляет данные из БД
     *
     * @param data
     */
    void delete(AuthorisationMailData data);

}
