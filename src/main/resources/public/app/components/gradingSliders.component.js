function gradingSlidersController($mdDialog) {

    this.calculate = student => {
        student.lock = true;

        let students = this.getUnlockedStudents();

        if (students.length === 0) {
            return;
        }

        const remaining = Math.round(this.getRemainingPoints() * 10) / 10;

        const mod = (remaining * 10) % students.length;

        if (mod !== 0) {
            for (let i = 0; i < mod; i++) {
                students[i].grade.grade = Math.ceil((remaining / students.length) * 10) / 10;
            }

            for (let i = mod; i < students.length; i++) {
                students[i].grade.grade = Math.floor((remaining / students.length) * 10) / 10;
            }
        } else {
            for (let i = 0; i < students.length; i++) {
                students[i].grade.grade = Math.floor((remaining / students.length) * 10) / 10;
            }
        }
    };

    this.getUnlockedStudents = () => {
        return this.students.filter(student => {
            return !student.lock;
        });
    };

    this.getRemainingPoints = () => {
        let lockedStudents = this.students.filter(student => {
            return student.lock;
        });

        let total = this.groupGrade * this.students.length;

        for (let i = 0; i < lockedStudents.length; i++) {
            total -= lockedStudents[i].grade.grade;
        }

        return total;
    };

    this.calculateCurrent = () => {
        let total = 0;

        for (let i = 0; i < this.students.length; i++) {
            total += this.students[i].grade.grade;
        }

        return total;
    };

    this.addMotivation = student => {
        let dialog = $mdDialog.prompt()
            .title("Add motivation")
            .textContent("Add your motivation for this grade here.")
            .initialValue(student.grade.motivation)
            .ok("Add")
            .cancel("Cancel");

        $mdDialog.show(dialog)
            .then(motivation => {
                student.grade.motivation = motivation;
            });

    }
}

app.component('gradingSliders', {
    templateUrl: '/app/components/gradingSliders.component.html',
    controller: gradingSlidersController,
    bindings: {
        students: '<',
        groupGrade: '<',
    },
});