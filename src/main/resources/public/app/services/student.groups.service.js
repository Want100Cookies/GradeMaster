app.factory('StudentGroupsService', function($http, $cookies, API){
    return {
        getStudentGroups : function(userId){
            return API.get({
                path: 'users/'+ userId +'/groups'
            });
        },

        getGroupMembers : function(groupId){
            return API.get({
                path: 'groups/'+ groupId +'/users'
            });
        },

        getFinalGroupGrade : function(groupId, userId){
            return API.get({
                path: 'grades/groups/'+ groupId +'/users/'+ userId
            });
        },

        getGradingStatus : function(groupId){
            return API.get({
                path: 'grades/status/groups/' + groupId
            });
        }
    }
});