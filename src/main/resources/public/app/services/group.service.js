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
    
    this.getGroupsByUserId = (userId) => {
        return API.get({
            path: `users/${userId}/groups`
        });
    };

    this.createGroup = (group) => {
        return API.post({
            path: `groups`,
            data: group
        });
    };

    this.editGroup = (data, groupId) => {
        return API.patch({
            path: `groups/${groupId}`,
            data
        })
    }
    this.editGroupUsers = (users, groupId) => {
        return API.patch({
            path: `groups/${groupId}/users`,
            data: users
        })
    }

    this.getGradingStatus = (groupId) => {
        return API.get({
            path: `grades/status/groups/${groupId}`
        });
    };

    this.getGroupMembers = (groupId) => {
        return API.get({
            path: `groups/${groupId}/users`
        });
    };

    this.deleteGroup = (groupId) => {
        return API.delete({
            path: `groups/${groupId}`
        });
    };
    
    return this;
});