function finalGroupOverviewController($stateParams, $state, GradeService, GroupService, UserService, $mdDialog, $http, $cookies) {
    let ctrl = this;
    ctrl.groupId = $stateParams.groupId;
    ctrl.groupMembers = [];
    ctrl.grades = [];
    ctrl.self = {};

    UserService.getSelf().then((response) => {
        ctrl.self = response.data;
    });

    GroupService.getGroupMembers(ctrl.groupId).then((response) => {
        ctrl.groupMembers = response.data.filter((user) => {
            return user.roles.filter((role) => {
                return role.code === "STUDENT_ROLE";
            }).length === 1
        });
        ctrl.getGrades();

    });

    ctrl.getGrades = () => {
        GradeService.getGradesByGroup(ctrl.groupId).then((response) => {
            ctrl.grades = response.data.grades;

            ctrl.groupMembers = ctrl.groupMembers.map((user) => {
                user.allowGrades = ctrl.getGradesForUser(user).length > 0;
                return user;
            });

            ctrl.checkAllowGrades();
        });
    };

    ctrl.checkAllowGrades = () => {
        for (let i = 0; i < ctrl.grades.length; i++) {
            if (!ctrl.grades[i].valid) {
                ctrl.groupMembers.filter((user) => {
                    return ctrl.grades[i].fromUser.id === user.id;
                }).map((user) => {
                    user.allowGrades = false;
                    return user;
                });
            }
        }
    };

    ctrl.getGradesForUser = (user) => {
        return ctrl.grades.filter((grade) => {
            return grade.fromUser.id === user.id;
        });
    };

    ctrl.showMotivation = (student, motivation) => {
        let dialog = $mdDialog.alert()
            .title(student + "'s motivation")
            .textContent(motivation)
            .ok("Ok")

        $mdDialog.show(dialog)

    };

    ctrl.getFinalGrade = (user) => {
        let grades = ctrl.grades.filter((grade) => {
            return grade.toUser.id === user.id;
        }).filter((grade) => {
            let fromUser = ctrl.groupMembers.filter((groupUser) => {
                return grade.fromUser.id === groupUser.id
            })[0];
            return fromUser.allowGrades;
        });

        let total = 0;

        for (let i = 0; i < grades.length; i++) {
            total += grades[i].grade;
        }

        return total / grades.length;
    };

    ctrl.save = () => {
        let grades = [];

        for (let i = 0; i < ctrl.groupMembers.length; i++) {
            grades.push({
                fromUser: {
                    id: ctrl.self.id,
                },
                toUser: {
                    id: ctrl.groupMembers[i].id
                },
                grade: ctrl.getFinalGrade(ctrl.groupMembers[i]),
                motivation: '-',
            })
        }

        GradeService.createFinalGrades(grades, ctrl.groupId)
            .then((response) => {
                GradeService.removeUserGrades(ctrl.groupId);
                $state.transitionTo("app.dashboard");
            }, (error) => {
                console.error(error);
            })
    };

    ctrl.export = (format) => {
        $http({
            url: 'http://localhost:8080/api/v1/grades/groups/' + ctrl.groupId + '/export.' + format,
            method: 'GET',
            params: {},
            headers: {
                'Content-type': 'application/pdf',
                'Authorization': 'Bearer ' + $cookies.get("access_token")
            },
            responseType: 'arraybuffer'
        }).then((data, status, headers, config) => {
            console.log(data);
            var file = new Blob([data.data], {
                type: 'application/csv'
            });
            var fileURL = URL.createObjectURL(file);
            var a = document.createElement('a');
            a.href = fileURL;
            a.target = '_blank';
            a.download = 'gradesheet.' + format;
            document.body.appendChild(a);
            a.click();
        }).catch((data, status, headers, config) => {
            console.log(data);
        });
    }

}

app.component('finalGroupOverview', {
    templateUrl: '/app/components/finalGroupOverview.component.html',
    controller: finalGroupOverviewController,
});