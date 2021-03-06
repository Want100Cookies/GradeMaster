
function GroupCardCtrl($state, StudentGroupsService, CourseService) {
    let ctrl = this;

    ctrl.groupMembers = [];
    ctrl.groupGrade = '';

    ctrl.$onInit = () => {
        ctrl.name = ctrl.group.groupName;

        ctrl.groupGrade = (ctrl.group.groupGrade == null) ? 'TBD' : ctrl.group.groupGrade.grade;

        StudentGroupsService.getFinalGroupGrade(ctrl.group.id, ctrl.user.id).then((response) => {
            if (response.data.grade) {
                ctrl.finalGrade = response.data.grade;
            }
        }).catch((error) => {
            if (error.status === 404) {
                ctrl.finalGrade = "TBD";
            } else {
                ctrl.finalGrade = "ERROR";
            }
        });

        StudentGroupsService.getGradingStatus(ctrl.group.id).then((response) => {
            ctrl.groupStatus = response.data.status;
            switch (ctrl.groupStatus) {
                case "INACTIVE":
                    ctrl.groupStatusClass = "grey-text";
                    break;
                case "OPEN":
                    ctrl.groupStatusClass = "blue-text";
                    break;
                case "PENDING":
                    ctrl.groupStatusClass = "yellow-text";
                    break;
                case "CLOSED":
                    ctrl.groupStatusClass = "red-text";
                    break;
            }
        });

        ctrl.calculateGradeChange = function(groupGrade, finalGrade){
            if (typeof finalGrade === "string") {
                ctrl.percentage = "";
                return "TBD";
            }
            const diff = finalGrade - groupGrade;
            const diffInPercentage = Math.round(diff / finalGrade * 100) + "%";

            if(diffInPercentage > 0){
                ctrl.percentage = "negative";
                return "+" + diffInPercentage;
            }
            else if(diffInPercentage === 0){
                ctrl.percentage = "equal";
                return diffInPercentage;
            }
            else{
                ctrl.percentage = "positive";
                return "+" + diffInPercentage;
            }
        }
    };

    ctrl.getGradeClass = (grade) => {
        if (typeof grade === "string") {
            return "";
        } else if (grade < 5.5) {
            return "red-text";
        } else if (grade === 5.5) {
            return "yellow-text";
        } else {
            return "blue-text";
        }
    };

    ctrl.gradeGroupMembers = () => {
        $state.transitionTo("app.grading", {groupId: ctrl.group.id});
    }
}

app.component('groupCard', {
    templateUrl: '/app/components/groupCard.component.html',
    controller: GroupCardCtrl,
    bindings: {
        user: '<',
        group: '<',
    }
});