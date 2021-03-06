package com.npn.spring.learning.spring.smallspringboot.model.dbservices.implementation;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.repositories.AuthorisationMailDataRepository;
import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.AuthorisationEmailExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Служба для работы с данными письма авторизации
 */
@Service
public class AuthorisationMailDataService implements AuthorisationMailDataInterface {

    @Autowired
    private AuthorisationMailDataRepository repository;

    @Autowired
    private UserServiceInterface userService;


    /**
     * Получает AuthorisationMailData из базы данных по его ID
     *
     * @param id id письма
     * @return AuthorisationMailData или null
     */
    @Override
    public AuthorisationMailData getAuthorisationMailDataById(final Long id) {
        return repository
                .findById(id)
                .orElse(null);
    }

    /**
     * Получает AuthorisationMailData из базы данных по ID пользователя
     *
     * @param userId id пользователя
     * @return AuthorisationMailData или null
     */
    @Override
    public AuthorisationMailData getAuthorisationMailDataByUserId(final Long userId) {
        return repository
                .findFirstByUserId(userId)
                .orElse(null);
    }

    /**
     * Сохраняет AuthorisationMailData в БД.
     *
     * @param data AuthorisationMailData
     */
    @Override
    public void save(final AuthorisationMailData data) {
        repository.save(data);
    }

    /**
     * Подтверждает авторизацию пользователя
     *
     * @param userNameToken      token for name of user
     * @param authorisationToken password token
     */
    @Override
    public boolean confirmAuthorisation(final String userNameToken, final String authorisationToken) throws AuthorisationEmailExpired {
        Date today = new Date();
        AuthorisationMailData data = getAuthorisationMailDataByUserNameToken(userNameToken);
        if (data!=null && !isExpired(data,today) ){
            if (data.getAuthorisationToken().equals(authorisationToken)){
                acceptRegistration(data);
                return true;
            }

        } else if (data!=null && isExpired(data,today)) {
            throw new AuthorisationEmailExpired();
        }

        return false;
    }

    /**
     * Получает AuthorisationMailData по userNameToken
     *
     * @param userNameToken token for name of user
     * @return AuthorisationMailData или null
     */
    @Override
    public AuthorisationMailData getAuthorisationMailDataByUserNameToken(String userNameToken) {
        return repository
                .findAll()
                .stream().filter(x->x.getUserNameToken().equals(userNameToken))
                .findFirst().orElse(null);
    }

    /**
     * Получает список всех AuthorisationMailData
     *
     * @return List<AuthorisationMailData>
     */
    @Override
    public List<AuthorisationMailData> getAllAuthorisationMailData() {
        return repository.findAll();
    }

    /**
     * Удаляет данные из БД
     *
     * @param data
     */
    @Override
    public void delete(AuthorisationMailData data) {
        repository.delete(data);
    }

    /**
     * Удаляет данные из БД
     *
     * @param userNameToken
     * @param authorisationToken
     */
    @Override
    public void delete(String userNameToken, String authorisationToken) {
        AuthorisationMailData data = repository.findFirstByAuthorisationTokenAndAndUserNameToken(authorisationToken,userNameToken);
        if (data!=null){
            delete(data);
        } else {
            throw new Error("Unexpected error, user not found");
        }
    }

    /**
     * Подтверждает регистрацию пользователя
     *
     * @param data
     */
    @Override
    public void acceptRegistration(AuthorisationMailData data) {
        User user = userService.findById(data.getUserId());
        if (user!=null){
            user.setEnabled(true);
            userService.saveUser(user);
        } else {
            throw new Error("Unexpected error, user not found");
        }
    }

    private boolean isExpired(AuthorisationMailData data, Date today){
        return today.getTime()>data.getExpiredDate();
    }


}
