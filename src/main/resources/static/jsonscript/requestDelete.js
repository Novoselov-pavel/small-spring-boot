/**
 * DELETE запрос
 * @param ref адрес запроса
 * @returns {Promise<Response | void>}  пустой ответ, если запрос имеет статус ok
 */
const requestDelete =(ref) => {
    return fetch(ref, {
        method: 'DELETE'
    }).then((response) => {
        if (response.ok) {
            return;
        } else {
            throw new Error('Response Not OK');
        }
    })
};