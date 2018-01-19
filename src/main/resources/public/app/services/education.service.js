app.factory('EducationService', function ($cookies, $q, $resource, $http, $state) {
    return {
        getCoursesByEducation: (education) => {
            const accessToken = $cookies.get("access_token");

            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/educations/"+education+"/courses", {
                    headers: {
                        "Authorization": "Bearer " + accessToken
                    }
                })
                .then(response => {
                    deferred.resolve(response.data);
                }, error => {
                    deferred.reject(error);
                });

            return deferred.promise;
        },
        getEducations: () => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/educations", {
                    headers: {
                        "Authorization": "Bearer " + accessToken
                    }
                })
                .then(response => {
                    deferred.resolve(response.data);
                }, error => {
                    deferred.reject(error);
                });

            return deferred.promise;
        },
    }
});