/**
 * Маппер соотвествия между объектом базы данных и модельной формы
 * пара ключ:значение. Ключ - имя свойства в объекте базы данных, значение - id модального окна
 * @type {{}}
 */
const objectToModalMenuMapper = {
    id: '#el_id',
    name: '#el_name',
    text: '#el_text',
    type: '#el_type',
    href: '#el_href',
    elementOrder: '#el_order'
};

/**
 * Адреса запросов
 * @type {string}
 */

const mainMenuJsonHref='/admin/mainMenu';
const saveElementHref = mainMenuJsonHref+"/saveNavMenuElement";


/**
 * Селекторы элементов формы
 * @type {string}
 */
const mainMenuTreeSelector='#jstree-menu-div';
const modalDivSelector ="#navMenuElementInfo";
const modalFormSelector = '.modifyElementForm';
const saveMenuItemSelector = '.saveNavElement';

/**
 * Селекторы кнопок
 * @type {string}
 */
const addNavMenuButtonSelector ='.addNavMenuButton';
const addNavChildElementButtonSelector = '.addNavChildElementButton';
const deleteNavElementSelector = '.deleteNavElement';
const modifyNavElementSelector = '.modifyNavElement';
const saveTreeSelector = '.saveTree';
const closeNavElementModalFormButton = '.closeNavElementModalFormButton';

const loadMenuTreeFromServer = () => {

    /**
     * Создает настройку для дерева, с переданными данными.
     *
     * @param data исходные данные для дерева
     * @returns {{}} объект настройки
     */
    const getJsTreeConfig = (data) =>{
        let typesObject = {
            "#" : {
                "max_depth" : 2,
                "valid_children" : ["MENU"]
            },
            "MENU" : {
                "valid_children" : ["ELEMENT","DIVIDER"]
            },
            "ELEMENT" : {
                "icon" : "/static/swg/app-indicator.svg",
                "valid_children" : []
            },
            "DIVIDER" : {
                "icon" : "/static/swg/distribute-vertical.svg",
                "valid_children" : []
            }
        };

        let jsTreeConfig = {
            "core": {
                'check_callback': function checkCallback(operation, node, node_parent, node_position, more) {
                    // operation can be 'create_node', 'rename_node', 'delete_node', 'move_node', 'copy_node' or 'edit'
                    //TODO
                    return true;
                },
                "themes" : { "stripes" : true },
                'data' : data
            },
            "types" : typesObject,
            "plugins" : [
                "types", "wholerow", "state", "dnd"
            ]
        };

        return jsTreeConfig;
    };

    /**
     * Создает обработчики для главного меню
     *
     */
    const addEventHandlerToButtons = () =>{
        $(modifyNavElementSelector).on('click', ()=>loadModalElMenu()
            .then(()=>getDataFromSelectedElement())
            .then((data)=>(data!==null)?fillModalMenuFromData(data): new Error('No rows selected'))
            .then(()=>addEventToSaveModalMenuButton(false))
            .then(()=>openModal())
            .catch((error)=>console.log(error)));
        $(addNavChildElementButtonSelector).on('click',()=>loadModalElMenu()
            .then(()=>clearModalMenu())
            .then(()=>addEventToSaveModalMenuButton(true))
            .then(()=>openModal())
            .catch(error=>console.log(error)));
        $(addNavMenuButtonSelector).on('click',()=>loadModalElMenu(false,false)
            .then(()=>clearModalMenu())
            .then(()=>addEventToSaveModalMenuButton(false))
            .then(()=>openModal())
            .catch(error=>console.log(error)));
        return "";
    };

    requestGetAndReturnJson(mainMenuJsonHref)
        .then((data)=>{
            function clickEvent(e,data) {
                let dataVal = JSON.stringify(data.node.original,null,4);
                $('.info-area').text(dataVal);
            }
            return $(mainMenuTreeSelector)
                .on('select_node.jstree', clickEvent)
                .jstree(getJsTreeConfig(data));
        })
        .then(()=>addEventHandlerToButtons())
        .catch((error)=>console.log(error));
};


/**
 * Создание пустого модального окна
 */
const loadModalElMenu =() =>{
    /**
     * Добавление модального окна всеми, существующими роля пользователя
     * @param data массив ролей пользователя
     */
    const addRoles = (data) => {
        let roleCount = 0;
        let form = $(modalFormSelector);
        form.children().filter('.div-roles').remove();
        // $('.div-roles').remove();
        data.forEach(x=>{
            let element = '<div class="form-check div-roles">' +
                '<input data-role="'+x.role+'" class="form-check-input roles-input" data-id="'+x.id+'" type="checkbox" value="" id="el_'+x.role+'">' +
                '<label class="form-check-label" for="el_'+x.role+'">' + x.role +'</label>' +
                '</div>';
            roleCount++;
            form.append(element);
        });
    }

    return getAllRoles()
        .then((result)=>addRoles(result));
};

/**
 * Открывает модальное окно
 */
const openModal = () =>{
    let buttonEl = "<button type=\"button\" class=\"btn btn-primary openNavMenuElementInfo002585\" hidden data-toggle=\"modal\" data-target=\""+modalDivSelector+"\"></button>";
    $(document.body).append(buttonEl);
    let button = $(".openNavMenuElementInfo002585");
    button.click();
    button.remove();
};
/**
 * очищает данные модального окна
 */
const clearModalMenu = () =>{
    for (let entry in objectToModalMenuMapper) {
        $(modalFormSelector).children().find(objectToModalMenuMapper[entry]).val("");
    }
    [...$(modalFormSelector)
        .find('.roles-input')
    ].forEach(x=>{
        $(x).prop('checked', false);
    });
}

/**
 * Заполнение модальной формы из переданного объекта
 * @param data объект опимывающий элемент меню
 */
const fillModalMenuFromData = (data) =>{
    for (let entry in objectToModalMenuMapper) {
        $(modalFormSelector).children().find(objectToModalMenuMapper[entry]).val(data[entry]);
    }
    let roles = data.authorities;
    roles.forEach(x=>$(modalFormSelector).find(`#el_${x.role}`).prop('checked',true));
};

/**
 * Возвращает данные, для заполнения модальной формы из выбранного объекта в дереве, или null если объект не выбран
 * @returns {Promise<{}>}
 */
const getDataFromSelectedElement = () =>{
    return new Promise(function (resolve, reject) {
        let tree = $(mainMenuTreeSelector).jstree(true);
        let selElements = tree.get_selected(true);
        if (selElements.length===0) resolve(null);
        let data = selElements[0].original;
        resolve(data);
    })
};

/**
 * добавляет событие к кнопке save модельной формы
 * @param isSaveChild определяет, сохраняется дочернее окно (true), или окно в текущей иерархии (false)
 */
const addEventToSaveModalMenuButton = (isSaveChild) =>{
    let parentId;
    let tree = $(mainMenuTreeSelector).jstree(true);
    if (isSaveChild) {
        parentId = tree.get_selected(true)[0].original.id;
    } else {
        parentId = tree.get_selected(true)[0].parent;
    }
    $(saveMenuItemSelector).on('click',()=>addElementToTreeData(parentId));
};

/**
 * Сохраняет элемент
 * @param parentId
 */
const addElementToTreeData = (parentId) => {
    $(closeNavElementModalFormButton).click();
    let tree = $(mainMenuTreeSelector).jstree(true);
    let data = getDataFromModal();
    data.parentId = parentId;

    let href = saveElementHref;
    return requestPutJson(href,data)
        .then(()=>{
            return requestGetAndReturnJson(mainMenuJsonHref)
                .then((data)=>{
                    tree.settings.core.data = data;
                    tree.refresh();
                    return "";
                });
        }).catch(error => console.log(error));
}
/**
 * Получает объект через
 * @returns {{}}
 */
const getDataFromModal = () => {
    let data = {};
    for (let entry in objectToModalMenuMapper) {
        data[entry] = $(modalFormSelector).children().find(objectToModalMenuMapper[entry]).val();
    }
    let roles = [];
    [...$(modalFormSelector)
        .find('.roles-input')
        .filter(function(){return $(this).prop('checked')})
    ].forEach(x=>{
        let role = {};
        role.id = $(x).attr('data-id');
        role.role = $(x).attr('data-role');
        roles.push(role);
    });
    data.authorities = roles;
    return data;
};