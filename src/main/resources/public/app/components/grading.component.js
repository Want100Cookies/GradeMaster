function gradingController($stateParams, $http, $cookies, $mdDialog, $state) {
    let ctrl = this;

    ctrl.groupGrade = 0;
    ctrl.students = [];
    ctrl.self = {};
    ctrl.loading = true;

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
            ctrl.students[i].grade = {
                grade: ctrl.groupGrade,
                motivation: "",
            };
        }
        ctrl.loading = false;
    });

    $http.get("http://localhost:8080/api/v1/users/self", {
        headers: {
            "Authorization": "Bearer " + accessToken
        }
    }).then(response => {
        ctrl.self = response.data;
    });

    ctrl.save = () => {
        let confirm = $mdDialog.confirm()
            .title("Are you sure?")
            .textContent("Are your sure you want to save these grades and remarks? You cannot edit these after saving!")
            .ok("Yes, I am sure the data I entered is correct!")
            .cancel("I don't know...");

        $mdDialog.show(confirm)
            .then(() => {
                ctrl.loading = true;
                let data = [];
                for (let i = 0; i < ctrl.students.length; i++) {
                    data.push({
                        fromUser: {id: ctrl.self.id},
                        toUser: {id: ctrl.students[i].id},
                        group: {id: $stateParams.groupId},
                        grade: ctrl.students[i].grade.grade,
                        motivation: ctrl.students[i].grade.motivation
                    })
                }

                $http.post("http://localhost:8080/api/v1/grades/users/" + ctrl.self.id, data, {
                    headers: {
                        "Authorization": "Bearer " + accessToken
                    }
                }).then(() => {
                    $state.transitionTo("app.groups");
                }, () => {
                    ctrl.loading = false;

                    $mdDialog.show($mdDialog.alert()
                        .title("Error")
                        .textContent("There was an error processing your request.")
                        .ok("Okay"));
                })
            });
    };
}

app.component('grading', {
    templateUrl: '/app/components/grading.component.html',
    controller: gradingController,
});