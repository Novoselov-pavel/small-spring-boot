/**
 * PUT запрос с данными представленными, как строка JSON.stringify()
 * @param href адрес запроса
 * @param data данные
 * @returns {Promise<Response>} пустой ответ, если запрос имеет статус ok
 */
const requestPutJson = (href, data) => {
    return fetch(href,{
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(data)
    }).then((response) =>{
        if (response.ok) {
            return;
        } else {
            throw new Error('Response Not OK');
        }
    });
};