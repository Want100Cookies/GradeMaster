app.factory('GradeService', function (API) {

    this.createGroupGrade = (group) => {
        let deadline = group.groupGrade.deadline.date;
        let timeString = group.groupGrade.deadline.time;
        deadline.setHours(timeString.split(":")[0], timeString.split(":")[1]);
        let groupGrade = {
            grade: group.groupGrade.grade,
            comment: group.groupGrade.comment,
            deadline: deadline.toISOString()
        };

        return API.patch({
            path: `grades/groups/${group.id}`,
            data: groupGrade
        });
    };

    this.createGrades = (students, user, group) => {
        let data = [];
        for (let i = 0; i < students.length; i++) {
            data.push({
                fromUser: {
                    id: user.id
                },
                toUser: {
                    id: students[i].id
                },
                group: {
                    id: group.id
                },
                grade: students[i].grade.grade,
                motivation: students[i].grade.motivation
            })
        }

        return API.post({
            path: `grades/groups/${group.id}`,
            data
        });
    };

    this.getGradesByGroup = (groupId) => {
        return API.get({
            path: `/grades/groups/`+groupId
        });
    }
    return this;
});