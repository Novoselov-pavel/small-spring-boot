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
 * Селекторы элементов формы
 * @type {string}
 */
const mainMenuTreeSelector='#jstree-menu-div';
const mainMenuJsonHref='/admin/mainMenu';
const modalDivSelector ="#navMenuElementInfo";
const modalFormSelector = '.modifyElementForm';
const saveMenuItemSelector = '.saveMenuItem';

/**
 * Селекторы кнопок
 * @type {string}
 */
const addNavMenuButtonSelector ='.addNavMenuButton';
const addNavElementButtonSelector = '.addNavElementButton';
const deleteNavElementSelector = '.deleteNavElement';
const modifyNavElementSelector = '.modifyNavElement';
const saveTreeSelector = '.saveTree';


const loadMenuTreeFromServer = () => {
    requestGetAndReturnJson(mainMenuJsonHref)
        .then((data)=>{
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

            function clickEvent(e,data) {
                let dataVal = JSON.stringify(data.node.original,null,4);
                $('.info-area').text(dataVal);
            }



            let jstreeConfig = {
                "core": {
                    "themes" : { "stripes" : true },
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

            let elem = [
                {
                    name: 'node1',
                    children: [
                        { name: 'child1' },
                        { name: 'child2' }
                    ]
                },
                {
                    name: 'node2',
                    children: [
                        { name: 'child3' }
                    ]
                }
            ];
            $(mainMenuTreeSelector)
                .on('select_node.jstree', clickEvent)
                .jstree(jstreeConfig);
        })
        .then(()=>{
            $(modifyNavElementSelector).on('click', ()=>loadModalElMenu(true))
            $(addNavElementButtonSelector).on('click',()=>loadModalElMenu(false,true));
            return $(addNavMenuButtonSelector).on('click',()=>loadModalElMenu(false,false));
        })
        .catch((error)=>console.log(error));
};

const createRootMenuItem = () =>{
    let tree = $(mainMenuTreeSelector).jstree(true);
    let selElement = tree.get_selected();
    loadModalElMenu(true); // temp

};


const loadModalElMenu =(isEdit, hasParentElement) =>{

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
                '<input name="'+roleCount+'" class="form-check-input" type="checkbox" value="" id="el_'+x.role+'">' +
                '<label class="form-check-label" for="el_'+x.role+'">' + x.role +'</label>' +
                '</div>';
            roleCount++;
            form.append(element);
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

    const addElementToTreeData = (parentId) => {
        let tree = $(mainMenuTreeSelector).jstree(true);
        if(!parentId) {
            //TODO stopped here
        } else {
            let parentObject = tree.get_node(parentId).original;
        }
    }

    return getAllRoles()
        .then(result=>{
            let buttonEl = "<button type=\"button\" class=\"btn btn-primary openNavMenuElementInfo\" hidden data-toggle=\"modal\" data-target=\""+modalDivSelector+"\"></button>";
            $(document.body).append(buttonEl);
            addRoles(result);
            let tree = $(mainMenuTreeSelector).jstree(true);

            if(isEdit) {
                let selElements = tree.get_selected(true);
                if (selElements.length===0) return;

                let data = selElements[0].original;
                fillModalMenuFromData(data);
            }
            if (hasParentElement) {
                let parentId = tree.get_selected(true)[0].parent;
                $(saveMenuItemSelector).on('click',()=>addElementToTreeData(parentId));
            } else {
                $(saveMenuItemSelector).on('click',()=>addElementToTreeData(false));
            }



            let button = $(".openNavMenuElementInfo");
            button.click();
            button.remove()
        })

};