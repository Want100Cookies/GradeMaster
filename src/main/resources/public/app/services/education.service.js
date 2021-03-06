app.factory('EducationService', function (API) {

    this.getCoursesByEducation = (educationId) => {
        return API.get({
            path: `educations/${educationId}/courses`
        });
    };

    this.getEducations = () => {
        return API.get({
            path: `educations`
        });
    };

    this.getEducation = (educationId) => {
        return API.get({
            path: `educations/${educationId}`
        });
    };

    this.updateEducation = (education) => {
        return API.patch({
            path: `educations/${education.id}`,
            data: education
        });
    };

    this.deleteEducation = (education) => {
        return API.delete({
            path: `educations/${education.id}`
        });
    };

    this.createEducation = (name) => {
        return API.post({
            path: `educations`,
            data: {name: name}
        });
    }

    return this;
});