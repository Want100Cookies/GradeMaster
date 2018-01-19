app.factory('CourseService', function (API) {
    return {
        getCourse: (courseId) => {
            return API.get({
                path: `courses/${courseId}`
            });
        },
        getGroupsByCourse: (courseId) => {
            return API.get({
                path: `courses/${courseId}/groups`
            })
        },
        deleteCourse: course => {
            return API.delete({
                path: `courses/${course.id}`
            });
        },
    }
});