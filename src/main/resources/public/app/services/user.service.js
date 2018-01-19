app.factory('UserService', function (API) {
    return {
        getSelf: () => {
            return API.get({
                path: `users/self`
            })
        },
        getAllUsers: function () {
            return API.get({
                path: `users`
            });
        },

        getUser : function(){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
                return data;
            }).catch(function(data){
                console.log(data);
            });
        }
    }
});