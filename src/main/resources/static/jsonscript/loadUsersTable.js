const loadUserTableFromJson = (tableBodyClass, jsonRef) =>{
    const mainRef = jsonRef;
    const tableBodyClassRef = tableBodyClass;
    const modalFormRef = 'form[class="modifyUserForm"]';

    /**
     * Заполнение таблицы и добавление обработчика двойного нажатия на строчку
     *
     * @param tableBodyClass класс тела таблицы
     * @param jsonObject массив пользователей
     */
    const fillTable = (tableBodyClass,jsonObject) =>{

        let tableBody = $(tableBodyClass);
        let count = 1;
        //Заполнение таблицы
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
        //Привязка обработчиков
        tableBody.find('tr').on('dblclick', function(e) {tableRowDblClickEvent(e);});

        tableBody.find('tr').on('click', function(e) {tableRowClickEvent(e);});

        $('body').on('keyup', function(e) {deleteKeyupEvent(e);});


    };

    /**
     * Открытие модельного окна, при двойном нажатии на строку таблицы
     * @param e
     */
    const tableRowDblClickEvent = (e) =>{
        let tableBody = $(tableBodyClassRef);
        tableBody.find('tr').removeClass('selectedRow');
        $(e.currentTarget).addClass('selectedRow');
        showModal(e);
    };

    /**
     * Выбор строки по click мыши
     *
     * @param e
     */
    const tableRowClickEvent = (e) =>{
        let tableBody = $(tableBodyClassRef);
        tableBody.find('tr').removeClass('selectedRow');
        $(e.currentTarget).addClass('selectedRow');
    };

    /**
     * обработчик нажатия кнопки delete
     * @param e
     */
    const deleteKeyupEvent = (e) => {
        //строка поиска выбраной строки в таблице
        const selectedSearchString ='.selectedRow';
        //строка поиска имени пользователя в таблице
        const searchUserNameString ='.user_name';
        //строка поиска id пользователя в таблице
        const searchIdString ='.id';
1
        const key = e.key;
        if (key === 'Delete') {
            let selectedRows = $(tableBodyClassRef).children().filter(selectedSearchString);
            if (selectedRows.length <= 0) return;
            let row = selectedRows.children();
            let name = row.filter(searchUserNameString).text();
            let id = row.filter(searchIdString).text();

            let acceptDelete = confirm(`delete user '${name}'?`);
            if (!acceptDelete) return;
            deleteUser(id)
                .then(()=> {
                    loadUserTableFromJson(tableBodyClassRef,mainRef);
                })
                .catch((error)=>console.log(error));
        }
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
            let form = $(modalFormRef);
            form.children().filter('.div-roles').remove();
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
         * Получает объект пользователя из модального окна для правки на сервер
         */
        const getDataForSaveUser = () => {
            let form = $(modalFormRef);

            let sendUser = {};
            sendUser.id = form.find('input[name="id"]').val();
            sendUser.name = form.find('input[name="name"]').val();
            sendUser.displayName = form.find('input[name="displayName"]').val();
            sendUser.accountNonExpired = form.find('input[name="accountNonExpired"]').prop('checked');
            sendUser.accountNonLocked = form.find('input[name="accountNonLocked"]').prop('checked');
            sendUser.credentialsNonExpired = form.find('input[name="credentialsNonExpired"]').prop('checked');
            sendUser.enabled = form.find('input[name="enabled"]').prop('checked');
            sendUser.roles = $.map(form.children().filter('.div-roles').children().filter(':checked'), (x) => $(x).prop('id'));
            return sendUser;
        };

        /**
         *  Открывает и заполняет модальное окно редактирование пользователя.
         *  Назначает обработчики для кнопки saveUser
         */
        getAllRoles()
            .then((result)=>{
                let buttonEl = "<button type=\"button\" class=\"btn btn-primary openModifyUser\" hidden data-toggle=\"modal\" data-target=\"#modifyUser\"></button>";
                $(document.body).append(buttonEl);
                loadUserDataWithoutRoles();
                addRoles(result);
                fillRoles();
                $(".saveUser").on('click', function () {
                    saveUser(getDataForSaveUser())
                        .then(()=>{
                            $('button[data-dismiss="modal"]').click();
                            loadUserTableFromJson(tableBodyClassRef,mainRef);
                        })
                        .catch((error) => {
                            console.log(error);
                            $('button[data-dismiss="modal"]').click();
                        });
                });
                let button = $(".openModifyUser");
                button.click();
                button.remove();
            })
            .catch((error)=>console.log(error));
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

    requestGetAndReturnJson(mainRef)
        .then((result)=>{
            clearTableBody(tableBodyClassRef);
            fillTable(tableBodyClassRef, result);
            return "";
        })
        .catch(reason => {console.log(reason)});
};