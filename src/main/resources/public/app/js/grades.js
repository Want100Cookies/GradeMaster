app.controller('GradesCtrl', function ($scope) {
    $scope.vm = {
        students: [],
        groupGrade: 6.1,
        change: function (student) {
            $scope.calculate(student);
        },
        totalCurrent: function () {
            let total = 0;

            for (let i = 0; i < $scope.vm.students.length; i++) {
                total += $scope.vm.students[i].grade;
            }

            return total;
        },
        total: function () {
            return $scope.vm.groupGrade * $scope.vm.students.length;
        }
    };

    for (let i = 0; i < 5; i++) {
        $scope.vm.students.push({
            "id": i,
            "name": "Student " + i,
            "grade": $scope.vm.groupGrade,
            "lock": false,
        });
    }

    $scope.calculate = function (student) {
        student.lock = true;
        let students = $scope.getUnlockedStudents().sort(function (a, b) {
            return a.grade - b.grade;
        });

        const length = students.length;

        const currentGoingUp = $scope.vm.totalCurrent() > $scope.vm.total();

        const index = currentGoingUp ? length - 1 : 0;

        students[index].grade += currentGoingUp ? -0.1 : 0.1;
    };

    $scope.getUnlockedStudents = function () {
        return $scope.vm.students.filter(function (student) {
            return !student.lock;
        });
    };

    $scope.getRemainingPoints = function () {
        let lockedStudents = $scope.vm.students.filter(function (student) {
            return student.lock;
        });

        let total = $scope.vm.total();

        for (let i = 0; i < lockedStudents.length; i++) {
            total -= lockedStudents[i].grade;
        }

        return total;
    };
});