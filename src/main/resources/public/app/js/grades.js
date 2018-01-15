app.controller('GradesCtrl', function ($scope) {
    $scope.vm = {
        groupGrade: 6.1,
        students: [],
    };


    for (let i = 0; i < 5; i++) {
        $scope.vm.students.push({
            "id": i,
            "name": "Student " + i,
            "grade": $scope.vm.groupGrade,
            "lock": false,
        });
    }
});