app.factory('GroupService', function ($cookies, $q, $resource, $http, $state) {
    return {
        createGroup: function (groupData) {
            var data = groupData;
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'POST',
                url: 'http://localhost:8080/api/v1/groups',
                headers: {
                    "Authorization": "Bearer " + accessToken,
                    "Content-type": "application/json"
                },
                data
            }
            $http(req).then(function (data) {
                console.log(data);
            }).catch(function (data){
                console.log(data);
            });
        }
    }
})