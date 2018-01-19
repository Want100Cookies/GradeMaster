app.factory('EducationService', function (API) {

    this.getCoursesByEducation = (education) => {
        const educationId = education; // TODO use education's id. 
        return API.get({
            path: `educations/${educationId}/courses`
        });
    };
  
    this.getEducations = () => {
        return API.get({
            path: `educations`  
        });
     };

    return this;
});