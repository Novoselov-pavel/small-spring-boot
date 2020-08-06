//строка ссылки на адрес действия с пользорвателем
const userRef ='/admin/user/';

/**
 * Удаление пользователя из таблицы
 * @param id id пользователя
 * @returns {Promise<Response | void>}
 * при ошибке
 */
const deleteUser = (id) => {
    return requestDelete(userRef+id);
};

/**
 * Сохранение пользователя
 * @returns {Promise<Response>}
 */
const saveUser = (user) => {
    let href = userRef + user.id;
    return requestPutJson(href,user);
};