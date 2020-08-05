const loadUserTableFromJson = (tableBodyClass, jsonRef) =>{
    const maxRequestNumber = 5;
    let currentRequestNumber = 0;
    const mainRef = jsonRef;
    const tableBodyClassRef = tableBodyClass;

    /**
     * Get запрос с предоставлением результата как объекта
     * @param ref дрес запроса
     * @returns {Promise<Response>} объект
     */
    const request = (ref) =>{
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
                    return request(ref);
                } else {
                    throw reject;
                }
            })
    };

    const deleteUser = () => {
        let selectedRows = $(tableBodyClassRef).children().filter('.selectedRow');
        if (selectedRows.length <= 0) return;

        let row = selectedRows.children();
        let name = row.filter('.user_name').text();
        let id = row.filter('.id').text();

        let acceptDelete = confirm(`delete user '${name}'?`);
        if (!acceptDelete) return;

        let href = $('form[class="modifyUserForm"]').prop('action') + id;
        return fetch(href, {
            method: 'DELETE'
        }).then((response) => {
            if (response.ok) {
                return loadUserTableFromJson(tableBodyClassRef, mainRef);
            } else {
                throw new Error('ResponseNotOK');
            }
        }).catch(error => console.log(error));

    };

    /**
     * Заполнение таблицы и добавление обработчика двойного нажатия на строчку
     *
     * @param tableBodyClass класс тела таблицы
     * @param jsonObject массив пользователей
     */
    const fillTable = (tableBodyClass,jsonObject) =>{
        let tableBody = $(tableBodyClass);
        let count = 1;
        for (let userInfo of jsonObject) {
            let roles = [];
            let authorities = userInfo.authorities;
            for (const authority of authorities) {
                roles.push(authority.role);
            }
            let html = "<tr>"+"<th scope=\"row\">"+count+"</th>" +
                "<td class=\"id\" hidden scope=\"col\">"+userInfo.id+"</td>" +
                "<td class=\"user_name\" scope=\"col\">"+userInfo.name+"</td>" +
                "<td class=\"user_display_name\" scope=\"col\">"+userInfo.displayName+"</td>" +
                "<td class=\"account_non_expired\" scope=\"col\">"+userInfo.accountNonExpired+"</td>" +
                "<td class=\"account_non_locked\" scope=\"col\">"+userInfo.accountNonLocked+"</td>" +
                "<td class=\"credentials_non_expired\" scope=\"col\">"+userInfo.credentialsNonExpired+"</td>" +
                "<td class=\"enabled\" scope=\"col\">"+userInfo.enabled+"</td>" +
                "<td class=\"roles\" scope=\"col\">"+roles+"</td>" + "</tr>";
            tableBody.append(html);
            count++;
        }
        tableBody.find('tr').on('dblclick', function (e) {
            tableBody.find('tr').removeClass('selectedRow');
            $(e.currentTarget).addClass('selectedRow');
            showModal(e);
        });

        tableBody.find('tr').on('click', function (e) {
            tableBody.find('tr').removeClass('selectedRow');
            $(e.currentTarget).addClass('selectedRow');
        });

        $('body').on('keyup', function (e) {
            const key = e.key;
            if (key === 'Delete') {
                deleteUser();
            }
        });


    };

    /**
     * Открывает и заполняет модальное окно редактирование пользователя
     * @param e параметр передающийся Handler
     */
    const showModal = (e) =>{
        /**
         * Заполнение окна редактирования пользователя
         * всеми данными, кроме ролей пользователя
         */
        const loadUserDataWithoutRoles = ()=>{
            let row = $(e.currentTarget);
            let id = row.find('.id').text();
            let name = row.find('.user_name').text();
            let displayName = row.find('.user_display_name').text();
            let accountNonExpired = row.find('.account_non_expired').text();
            let accountNonLocked = row.find('.account_non_locked').text();
            let credentialsNonExpired = row.find('.credentials_non_expired').text();
            let enabled = row.find('.enabled').text();

            let modifyUserBody = $('.modifyUserBody');
            modifyUserBody.find('input[name="id"]').val(id);
            modifyUserBody.find('input[name="name"]').val(name);
            modifyUserBody.find('input[name="displayName"]').val(displayName);
            modifyUserBody.find('input[name="accountNonExpired"]').prop('checked', accountNonExpired);
            modifyUserBody.find('input[name="accountNonLocked"]').prop('checked', accountNonLocked);
            modifyUserBody.find('input[name="credentialsNonExpired"]').prop('checked', credentialsNonExpired);
            modifyUserBody.find('input[name="enabled"]').prop('checked', enabled);
        };

        /**
         * Добавление модального окна всеми, существующими роля пользователя
         * @param data массив ролей пользователя
         */
        const addRoles = (data) => {
            let roleCount = 0;
            let form = $('form[class="modifyUserForm"]');
            $('.div-roles').remove();
          data.forEach(x=>{
              let element = '<div class="form-check div-roles">' +
                  '<input name="role'+roleCount+'" class="form-check-input" type="checkbox" value="" id="'+x.role+'">' +
                  '<label class="form-check-label" for="'+x.role+'">' + x.role +'</label>' +
                  '</div>';
              roleCount++;
              form.append(element);
          });
        }

        /**
         * Отмечает роли, которые есть у пользователя
         */
        const fillRoles = () =>{
            let row = $(e.currentTarget);
            let roles = row.find('.roles').text().split(',');
            roles.forEach(x=>{
               $('input[id="'+x+'"]').prop('checked',true);
            });
        }

        /**
         *  Открывает и заполняет модальное окно редактирование пользователя.
         *  Назначает обработчики для кнопки saveUser
         */
        request('admin/getAllRoles')
            .then((result)=>{
                let buttonEl = "<button type=\"button\" class=\"btn btn-primary openModifyUser\" hidden data-toggle=\"modal\" data-target=\"#modifyUser\"></button>";
                $(document.body).append(buttonEl);
                loadUserDataWithoutRoles();
                addRoles(result);
                fillRoles();
                $(".saveUser").on('click', function () {
                    saveUser()
                        .then((response)=>{
                            $('button[data-dismiss="modal"]').click();
                            if (response.ok) {
                                return loadUserTableFromJson(tableBodyClassRef,mainRef);
                            } else {
                                throw new Error('ResponseNotOK');
                            }
                        })
                        .catch((reason => console.log(reason)));
                })

                $(".openModifyUser").click();
                $(".openModifyUser").remove();
            })


    };

    /**
     * Сохранение пользователя
     * @returns {Promise<Response>}
     */
    const saveUser = () => {
        let form = $('form[class="modifyUserForm"]');
        let id = form.find('input[name="id"]').val();
        let href = $('form[class="modifyUserForm"]').prop('action') + id;

        let sendUser = {};
        sendUser.id = id;
        sendUser.name = form.find('input[name="name"]').val();
        sendUser.displayName = form.find('input[name="displayName"]').val();
        sendUser.accountNonExpired = form.find('input[name="accountNonExpired"]').prop('checked');
        sendUser.accountNonLocked = form.find('input[name="accountNonLocked"]').prop('checked');
        sendUser.credentialsNonExpired = form.find('input[name="credentialsNonExpired"]').prop('checked');
        sendUser.enabled = form.find('input[name="enabled"]').prop('checked');
        let roles = $.map($('.div-roles').children().filter(':checked'),(x)=>$(x).prop('id'));
        sendUser.roles = roles;

        return fetch(href,{
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json;charset=UTF-8'
            },
            body: JSON.stringify(sendUser)
        })
    };

    /**
     * Очистка тела таблицы
     *
     * @param tableBodyClass
     */
    const clearTableBody = (tableBodyClass) => {
        let tableBody = $(tableBodyClass);
        tableBody.children().remove();
    }

    request(mainRef)
        .then((result)=>{
            clearTableBody(tableBodyClassRef);
            fillTable(tableBodyClassRef, result);
            return "";
        })
        .catch(reason => {console.log(reason)})
};