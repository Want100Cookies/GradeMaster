app.factory('StudentGroupsService', function(API){

    this.getStudentGroups = (userId) => {
        return API.get({
            path: `users/${userId}/groups`
        });
    };

    this.getGroupMembers = (groupId) => {
        return API.get({
            path: `groups/${groupId}/users`
        });
    };

    this.getFinalGroupGrade = (groupId, userId) => {
        return API.get({
            path: `grades/groups/${groupId}/users/${userId}`
        });
    };

    this.getGradingStatus = (groupId) => {
        return API.get({
            path: `grades/status/groups/${groupId}`
        });
    };

    return this;
});