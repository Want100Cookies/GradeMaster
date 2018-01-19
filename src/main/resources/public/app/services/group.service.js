app.factory('GroupService', function (API, $state) {

    this.getGroup = (id) => {
        return API.get({
            path: `groups/${id}`
        });
    };

    this.getGroups = () => {
        return API.get({
            path: `groups`
        })
    }
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
        return API.post({
            path: `groups`,
            data: group
        });
    };
    
    this.getGradingStatus = (groupId) => {
        return API.get({
            path: 'grades/status/groups/' + groupId
        });
    };

    this.getGroupMembers = (groupId) => {
        return API.get({
            path: 'groups/'+ groupId +'/users'
        });
    };
    
    return this;
});