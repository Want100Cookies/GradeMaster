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
        let diff = $scope.vm.groupGrade - student.grade;

        let students = $scope.getUnlockedStudents();
        let length = students.length;
        let change = 0;

        if (Math.abs(diff * 10) < students.length) {
            change = (diff > 0) ? -0.1 : 0.1;
            length = Math.abs(diff) * 10;

        } else {
            change = diff / students.length;
        }

        const remaining = $scope.getRemainingPoints();

        for (let i = 0; i < length; i++) {
            students[i].grade = (remaining / students.length) + change;
        }
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