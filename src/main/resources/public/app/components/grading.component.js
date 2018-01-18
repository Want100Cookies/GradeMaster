function gradingController($stateParams, $http, $cookies) {
    let ctrl = this;

    ctrl.groupGrade = 0;
    ctrl.students = [];

    let accessToken = $cookies.get("access_token");
    $http.get("http://localhost:8080/api/v1/groups/" + $stateParams.groupId, {
        headers: {
            "Authorization": "Bearer " + accessToken
        }
    }).then(response => {
        ctrl.groupGrade = response.data.groupGrade.grade;
        ctrl.students = response.data.users.filter(user => {
            return user.roles.filter(role => {
                return role.code === "STUDENT_ROLE"
            }).length === 1;
        });

        for (let i = 0; i < ctrl.students.length; i++) {
            ctrl.students[i].grade = ctrl.groupGrade;
        }
    });
    console.log($stateParams);

}

app.component('grading', {
    templateUrl: '/app/components/grading.component.html',
    controller: gradingController,
});