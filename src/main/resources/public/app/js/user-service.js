app.factory('UserService', function($cookies, $q, $resource, $http, $state){
    return {
        getRole : function(){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
               for(key in data.data.roles) {
                   var role = data.data.roles[key].label; 
               }
               return role;
            }).catch(function(data){
                console.log(data);
            });
        }
    }
});