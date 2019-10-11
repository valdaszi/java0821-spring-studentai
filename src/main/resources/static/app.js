window.addEventListener('DOMContentLoaded', run);

function run() {
    console.log("Started...");
    loadStudents(10, 1);
}

async function loadStudents(pageSize, pageNo) {
    const response = await fetch('/api');
    const students = await response.json();
    console.log(students);

    showStudents(students);
}

function showStudents(students) {
    let element = document.getElementById("title");
    element.innerHTML = "List of Students"
    let container = document.getElementById("container")
    let table = 'No data'
    if (Array.isArray(students)) {
        table = `
            <table class="table">
                <tr>
                    <th>id</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>e-mail</th>
                </tr>
        `

        students.forEach(student => {
            table += `
                <tr onclick="loadStudent(${student.id})">
                    <td>${student.id}</></td>
                    <td>${student.firstName}</td>
                    <td>${student.lastName}</td>
                    <td>${student.email}</td>
                </tr>    
            `
        })

        table += '</table>'
    }

    container.innerHTML = table;
}

async function loadStudent(id) {
    const response = await fetch('/api/' + id);
    const student = await response.json();
    console.log(student);

    showStudent(student);
}

function showStudent(student) {
    let element = document.getElementById("title");
    element.innerHTML = "Student #" + student.id

    let studentInfo = `    
        <div class="container">
            <div class="row">
                <div class="col-4">id</div>
                <div class="col-8"><strong>${student.id}</strong></div>
            </div>
            <div class="row">
                <div class="col-4">First name</div>
                <div class="col-8"><strong>${student.firstName}</strong></div>
            </div>
            <div class="row">
                <div class="col-4">Last name</div>
                <div class="col-8"><strong>${student.lastName}</strong></div>
            </div>
            <div class="row">
                <div class="col-4">e-mail</div>
                <div class="col-8"><strong>${student.email}</strong></div>
            </div>
        </div>   
    `

    if (Array.isArray(student.grades)) {
        let gradesInfo = `
            <div class="container">
            <table class="table">
                <tr>
                    <th>Id</th>
                    <th>Date</th>
                    <th>Grade</th>
                </tr>
        `
        student.grades.forEach(grade => {
            gradesInfo += `
                <tr>
                    <td>${grade.id}</td>
                    <td>${grade.date}</td>
                    <td>${grade.grade}</td>
                </tr>
            `
        })
        gradesInfo += '</table></div>'
        studentInfo += gradesInfo
    }

    let container = document.getElementById("container")
    container.innerHTML = studentInfo
}
