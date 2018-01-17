app.factory('UserService', function($http, $cookies){
    return {
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
                console.log("Succesful API Call", data);
                return data;
            }).catch(function(data){
                console.log("ERROR", data);
            });
        }
    }
});