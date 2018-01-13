app.controller('GradesCtrl', function ($scope) {
    const groupGrade = 6.1;
    let students = [];

    for (let i = 0; i < 5; i++) {
        students.push({
            "id": i,
            "name": "Student " + i,
            "grade": groupGrade,
            "lock": false,
        });
    }

    const totalPoints = groupGrade * students.length;

    $scope.vm = {
        students: students,
        groupGrade: groupGrade,
        change: function (student) {
            $scope.calculate(student);
        },
        totalCurrent: function () {
            return $scope.calculateCurrent();
        },
        toggleLock: function (student) {
            student.lock = !student.lock;
        },
    };

    $scope.calculate = function (student) {
        student.lock = true;

        let students = $scope.getUnlockedStudents();

        if (students.length === 0) {
            return;
        }

        // const currentGoingUp = $scope.calculateCurrent() > totalPoints;
        //
        // while (Math.round($scope.calculateCurrent() * 10) / 10 !== totalPoints) {
        //     students[index].grade += currentGoingUp ? -0.1 : 0.1;
        //     index += 1;
        //
        //     if (index > students.length-1) {
        //         index = 0;
        //     }
        // }

        const remaining = Math.round($scope.getRemainingPoints() * 10) / 10;

        console.log(remaining, remaining / students.length, (remaining * 10) % students.length);

        const mod = (remaining * 10) % students.length;

        if (mod !== 0) {
            for (let i = 0; i < mod; i++) {
                students[i].grade = Math.ceil((remaining / students.length) * 10) / 10;
            }

            for (let i = mod; i < students.length; i++) {
                students[i].grade = Math.floor((remaining / students.length) * 10) / 10;
            }
        } else {
            for (let i = 0; i < students.length; i++) {
                students[i].grade = Math.floor((remaining / students.length) * 10) / 10;
            }
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

        let total = totalPoints;

        for (let i = 0; i < lockedStudents.length; i++) {
            total -= lockedStudents[i].grade;
        }

        return total;
    };

    $scope.calculateCurrent = function () {
        let total = 0;

        for (let i = 0; i < $scope.vm.students.length; i++) {
            total += $scope.vm.students[i].grade;
        }

        return total;
    };
});