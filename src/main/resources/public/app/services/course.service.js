app.factory('CourseService', function (API) {

    this.getCourse = (courseId) => {
        return API.get({
            path: `courses/${courseId}`
        });
    };

    this.getGroupsByCourse = (courseId) => {
        return API.get({
            path: `courses/${courseId}/groups`
        })
    };

    this.deleteCourse = course => {
        return API.delete({
            path: `courses/${course.id}`
        });
    };

    return this;
});