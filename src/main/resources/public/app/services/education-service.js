app.factory('EducationService', function ($cookies, $q, $resource, $http, $state) {
    return {
        getCoursesByEducation : function(education) {
            var accessToken = $cookies.get("access_token");
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/educations/1/courses',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }
            return $http(req).then(function (data) {
                return data;
            }).catch(function (data) {
                console.log(data);
            });
        } 
    }
})