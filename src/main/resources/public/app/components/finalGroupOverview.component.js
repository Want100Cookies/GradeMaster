function finalGroupOverviewController($stateParams, GradeService, GroupService, $mdDialog, $http, $cookies) {
    let ctrl = this;
    ctrl.groupId = $stateParams.groupId;
    ctrl.groupMembers = [];
    ctrl.grades = [];
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
        })
    };

    ctrl.getGradesForUser = (user) => {
        return ctrl.grades.filter((grade) => {
            return grade.fromUser.id === user.id;
        });
    }

    ctrl.showMotivation = (student, motivation) => {
        let dialog = $mdDialog.alert()
            .title(student + "'s motivation")
            .textContent(motivation)
            .ok("Ok")

        $mdDialog.show(dialog)

    }

    ctrl.export = (format) => {
       $http({
            url: 'http://localhost:8080/api/v1/grades/groups/'+ctrl.groupId+'/export.'+format,
            method: 'GET',
            params: {},
            headers : {
                'Content-type' : 'application/pdf',
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
            a.download = 'gradesheet.'+format;
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