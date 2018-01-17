app.factory('StudentGroupsService', function($http, $cookies){
    return {
        getStudentGroups : function(userId){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/'+ userId +'/groups',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
                console.log("Succesful getStudentGroups API Call", data);
                return data;
            }).catch(function(data){
                console.log("ERROR", data);
            });
        },

        getAllGroupMembers : function(groupId){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/groups/'+ groupId +'/users',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
                console.log("Succesful getAllGroupMembers API Call", data);
                return data;
            }).catch(function(data){
                console.log("ERROR", data);
            });
        }
    }
});