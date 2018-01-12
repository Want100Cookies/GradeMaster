app.factory('AuthService', function($cookies, $q, $location, $resource, $http, $httpParamSerializer){
    return {
        authenticate : function(){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: '/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
                return true;
                console.log(data);
            }).catch(function(data){
                return $q.reject('Not Authenticated')
                console.log(data);
            });
        }
    }
});