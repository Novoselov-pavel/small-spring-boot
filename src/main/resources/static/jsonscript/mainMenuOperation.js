const mainMenuTreeSelector='#jstree-menu-div';
const mainMenuJsonHref='/admin/mainMenu';

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
                    "valid_children" : []
                },
                "DIVIDER" : {
                    "valid_children" : []
                }
            };
            function checkCallback(operation, node, node_parent, node_position, more) {
                // operation can be 'create_node', 'rename_node', 'delete_node', 'move_node', 'copy_node' or 'edit'
                //TODO
                return true;
            }
            let jstreeConfig = {
                "core": {
                    'check_callback': checkCallback(operation, node, node_parent, node_position, more),
                    "themes" : { "stripes" : true },
                    'data' : data
                },
                "types" : typesObject,
                "plugins" : [
                    "state", "types", "unique", "wholerow"
                ]
            };
            $(mainMenuTreeSelector).jstree(jstreeConfig);
        }).catch((error)=>console.log(error));
};