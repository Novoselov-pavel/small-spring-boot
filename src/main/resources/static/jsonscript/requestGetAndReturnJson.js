const maxRequestNumber = 5;
let currentRequestNumber = 0;
/**
 * Get запрос с предоставлением результата как объекта
 * @param ref адрес запроса
 * @returns {Promise<Response>}  пустой ответ, если запрос имеет статус ok
 */
const requestGetAndReturnJson = (ref) =>{
    return fetch(ref, {credentials: 'include'})
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Response not OK');
            }
        })
        .catch((reject) => {
            if (currentRequestNumber < maxRequestNumber) {
                currentRequestNumber += 1;
                return requestGetAndReturnJson(ref);
            } else {
                throw reject;
            }
        })
};