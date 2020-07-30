const loadUserTableFromJson = (tableBodyClass, jsonRef) =>{
    const maxRequestNumber = 5;
    let currentRequestNumber = 0;

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

    const fillTable = (tableBodyClass,jsonObject) =>{
        let tableBody = $(tableBodyClass);
        let count = 1;
        for (let userInfo of jsonObject) {
            let Roles = "";
            let authorities = userInfo.authorities;
            for (const authority of authorities) {
                Roles += authority.role + " ";
            }
            let html = "<tr>"+"<th scope=\"row\">"+count+"</th>" +
                "<td class=\"user_name\" scope=\"col\">"+userInfo.name+"</td>" +
                "<td class=\"user_display_name\" scope=\"col\">"+userInfo.displayName+"</td>" +
                "<td class=\"account_non_expired\" scope=\"col\">"+userInfo.accountNonExpired+"</td>" +
                "<td class=\"account_non_locked\" scope=\"col\">"+userInfo.accountNonLocked+"</td>" +
                "<td class=\"credentials_non_expired\" scope=\"col\">"+userInfo.credentialsNonExpired+"</td>" +
                "<td class=\"enabled\" scope=\"col\">"+userInfo.enabled+"</td>" +
                "<td class=\"roles\" scope=\"col\">"+Roles+"</td>" + "</tr>";
            tableBody.append(html);
            count++;
        }
    };

    request(jsonRef)
        .then((result)=>{
            fillTable(tableBodyClass, result);
            return "";
        })
};