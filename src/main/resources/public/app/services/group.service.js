app.factory('GroupService', function (API, $state) {

    this.getGroup = (id) => {
        return API.get({
            path: `groups/${id}`
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
            path: `users/${user.id}`,
            data
        });
    };

    this.createGroup = (group) => {
        const {groupName, startYear, endYear, users} = group;
        const course = 1;
        const period = [`Q1`, `Q2`];
        const data = {
            groupName,
            startYear,
            endYear,
            users,
            course,
            period
        };

        return API.post({
            path: `groups`,
            data
        });
    };

    return this;
});