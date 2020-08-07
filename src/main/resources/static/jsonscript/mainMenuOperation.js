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
        }).catch((error)=>console.log(error));
};