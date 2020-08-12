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
const deleteElementHref = mainMenuJsonHref+"/deleteNavMenuElement/";

/**
 * Селекторы элементов формы
 * @type {string}
 */
const mainMenuTreeSelector='#jstree-menu-div';
const modalDivSelector ="#navMenuElementInfo";
const modalFormSelector = '#modifyElementForm';
const saveMenuItemSelector = '.saveNavElement';

/**
 * Селекторы кнопок
 * @type {string}
 */
const addNavMenuButtonSelector ='.addNavMenuButton';
const addNavChildElementButtonSelector = '.addNavChildElementButton';
const deleteNavElementSelector = '.deleteNavElement';
const modifyNavElementSelector = '.modifyNavElement';
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
                    if (operation === 'move_node'|| operation === 'rename_node' || operation === 'copy_node' || operation === 'edit') {
                        return false;
                    }
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
            .then(()=>clearModalMenu())
            .then(()=>getDataFromSelectedElement())
            .then((data)=>(data!==null)?fillModalMenuFromData(data): new Error('No rows selected'))
            .then(()=>addEventToSaveModalMenuButton(false))
            .then(()=>openModal())
            .catch((error)=>console.log(error)));
        $(addNavChildElementButtonSelector).on('click',()=>!isCanCreateChild()? null : loadModalElMenu()
            .then(()=>clearModalMenu())
            .then(()=>addEventToSaveModalMenuButton(true))
            .then(()=>openModal())
            .catch(error=>console.log(error)));
        $(addNavMenuButtonSelector).on('click',()=>loadModalElMenu(false,false)
            .then(()=>clearModalMenu())
            .then(()=>addEventToSaveModalMenuButton(false))
            .then(()=>openModal())
            .catch(error=>console.log(error)));
        $(deleteNavElementSelector).on('click',()=>deleteModalElMenu());
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
 * Проверяет, можно ли создать дочерний элемент
 * @return {boolean}
 */
const isCanCreateChild = () =>{
    let tree = $(mainMenuTreeSelector).jstree(true);
    let selElements = tree.get_selected(true);
    if (selElements.length===0) return false;
    let data = selElements[0].original;

    return data.type === 'MENU';
};

/**
 * Удаляет элемент
 * @returns {Promise<void>}
 */
const deleteModalElMenu = () => {
    return new Promise(function (resolve, reject) {
        let tree = $(mainMenuTreeSelector).jstree(true);
        let selElements = tree.get_selected(true);
        if (selElements.length===0) return resolve(null);

        let id = selElements[0].original.id;
        let name = selElements[0].original.text;
        let acceptDelete = confirm(`Удалить элемент меню '${name}'?`);
        if (!acceptDelete) return resolve(null);
        return requestDelete(deleteElementHref+id)
            .then(()=>requestGetAndReturnJson(mainMenuJsonHref))
            .then((data)=>{
                tree.settings.core.data = data;
                tree.refresh();
                return "";
            }).catch(error=>console.log(error));
    });
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
    let form = $(modalFormSelector);
    for (let entry in objectToModalMenuMapper) {
        form.children().find(objectToModalMenuMapper[entry]).val("");
    }
    [...form.find('.roles-input')].forEach(x=>{
        $(x).prop('checked', false);
    });
    form.find('#el_name').removeClass('is-invalid');
    form.find('#el_type').removeClass('is-invalid');
    form.find('#el_order').removeClass('is-invalid');
    form.find('#el_text').removeClass('is-invalid');
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
    $(modalFormSelector).on('submit', function (event) {
        event.preventDefault();
        if (!isModalFormReady(parentId)){
            event.stopPropagation();
        } else {
            addElementToTreeData(parentId);
        }
    })
    // $(saveMenuItemSelector).on('click',()=>addElementToTreeData(parentId));
};

/**
 * Проверяет форму на правильность, true если форма верна, false если есть ошибки.
 * @return boolean
 */
const isModalFormReady = (parentId) =>{
    let tree = $(mainMenuTreeSelector).jstree(true);
    let data = tree.settings.core.data;
    let form = $(modalFormSelector);
    let id = form.find('#el_id').val();

    const reducer = (value) => {
        return [value,...value.children.flatMap((x)=>reducer(x))];
    };
    let flatData = data.flatMap((x)=>reducer(x));

    const isNameValid = (id) => {
        let name = form.find('#el_name').val();
        if (name === '') return false;

        let filterData = flatData.filter(x=>x.name === name);
        if (filterData.length>1) {
            return false;
        } else if (filterData.length === 1 && filterData[0].id.toString()!==id){
            return false;
        }
        return true;
    };

    const isTypeValid = (parentId) =>{
        let type = form.find('#el_type').val();
        if (parentId==='#'){
            return type!=='DIVIDER';
        } else {
            return type!=='MENU';
        }
    };

    const isOrderValid = () =>{
        let order = form.find('#el_order').val();

        return /^\d+$/.test(order);
    };

    const isTextValid = () =>{
        let text = form.find('#el_text').val();
        return text !== '';
    };

    let retVal = true;

    if (!isNameValid(id)){
        form.find('#el_name').addClass('is-invalid');
        retVal = false;
    } else {
        form.find('#el_name').removeClass('is-invalid');
    }
    if (!isTypeValid(parentId)){
        form.find('#el_type').addClass('is-invalid');
        retVal = false;
    } else {
        form.find('#el_type').removeClass('is-invalid');
    }

    if (!isOrderValid()){
        form.find('#el_order').addClass('is-invalid');
        retVal = false;
    } else {
        form.find('#el_order').removeClass('is-invalid');
    }

    if (!isTextValid()){
        form.find('#el_text').addClass('is-invalid');
        retVal = false;
    } else {
        form.find('#el_text').removeClass('is-invalid');
    }
    return retVal;
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