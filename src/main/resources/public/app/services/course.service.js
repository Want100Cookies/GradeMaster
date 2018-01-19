app.factory('CourseService', function ($cookies, $q, $resource, $http, $state) {
    return {
        getCourse: courseId => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/courses/" + courseId, {
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
        getGroupsByCourse: courseId => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .get("http://localhost:8080/api/v1/courses/" + courseId + "/groups", {
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
        deleteCourse: course => {
            const accessToken = $cookies.get("access_token");
            let deferred = $q.defer();

            $http
                .delete("http://localhost:8080/api/v1/courses/" + course.id, {
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
    }
});