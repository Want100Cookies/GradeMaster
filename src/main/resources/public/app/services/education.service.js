app.factory('EducationService', function ($cookies, $q, $resource, $http) {
    return {
        getCoursesByEducation: (educationId) => {
            const accessToken = $cookies.get("access_token");

            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/educations/" + educationId + "/courses", {
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
        getEducation: educationId => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/educations/" + educationId, {
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
        updateEducation: education => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .patch("http://localhost:8080/api/v1/educations/" + education.id, education, {
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
        deleteEducation: education => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .delete("http://localhost:8080/api/v1/educations/" + education.id, {
                    headers: {
                        "Authorization": "Bearer " + accessToken
                    }
                })
                .then(() => {
                    deferred.resolve();
                }, error => {
                    deferred.reject(error);
                });

            return deferred.promise;
        },
        createEducation: name => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .post("http://localhost:8080/api/v1/educations", {
                    name: name,
                }, {
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