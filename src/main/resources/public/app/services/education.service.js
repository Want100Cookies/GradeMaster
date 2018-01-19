app.factory('EducationService', function (API) {

    this.getCoursesByEducation = (education) => {
        const educationId = 1; // TODO use education's id. 
        return API.get({
            path: `educations/${educationId}/courses`
        });
    }

    return this;
});