const getAllRolesHref = 'admin/getAllRoles';

/**
 * Возврашает все роли в виде массива объектов
 * @returns {Promise<Response>} массив объектов
 */
const getAllRoles = () =>{
    return requestGetAndReturnJson(getAllRolesHref);
}