app.factory('GroupService', function (API) {

    this.getGroup = (id) => {
        return API.get({
            path: `groups/${id}`
        });
    };

    this.getGroups = () => {
        return API.get({
            path: `groups`
        })
    };

    this.createGroup = (group) => {
        return API.post({
            path: `groups`,
            data: group
        });
    };

    return this;
});