app.factory('AuthService', function($cookies, $q, $location, $resource, $http, $httpParamSerializer, $state){
    return {
        authenticate : function(){
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://192.168.1.128:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function(data){
                return true;
                console.log(data);
            }).catch(function(data){
                $state.transitionTo('login')
            });
        }
    }
});