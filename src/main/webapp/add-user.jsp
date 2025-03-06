<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add User</title>
    <link rel="stylesheet" href="styles.css">
    <script>
        function updateClassSelection() {
            var role = document.getElementById("role").value;
            var teacherClasses = document.getElementById("teacher-classes");
            var studentClass = document.getElementById("student-class");

            if (role === "TEACHER") {
                teacherClasses.style.display = "block";
                studentClass.style.display = "none";
            } else {
                teacherClasses.style.display = "none";
                studentClass.style.display = "block";
            }
        }
    </script>
</head>
<body>
<div class="sidebar">
    <div class="logo">
        <img src="logo.png" alt="Logo">
    </div>
    <ul>
        <li><a href="admin-dashboard.jsp"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>
        <li class="active"><i class="fas fa-user-plus"></i> <span>Add User</span></li>
        <li><a href="add-course.jsp"><i class="fas fa-book"></i> <span>Add Course</span></a></li>
        <li><i class="fas fa-sign-out-alt"></i> <a href="logout">Logout</a></li>
    </ul>
</div>

<div class="main-content">
    <div class="content-box">
        <h2>Add New User</h2>
        <!-- Add a form to allow the admin to add a new user -->
        <form action="AdminServlet" method="post">
            <label>Username:</label>
            <input type="text" name="username" required>

            <label>Password:</label>
            <input type="password" name="password" required>

            <label>Role:</label>
            <select name="role" id="role" onchange="updateClassSelection()" required>
                <option value="STUDENT">Student</option>
                <option value="TEACHER">Teacher</option>
            </select>

            <div id="teacher-classes" style="display: none;">
                <label>Select Classes (For Teachers):</label><br>
                <input type="checkbox" name="class" value="Year 1A"> Year 1A<br>
                <input type="checkbox" name="class" value="Year 1B"> Year 1B<br>
                <input type="checkbox" name="class" value="Year 1C"> Year 1C<br>
                <input type="checkbox" name="class" value="Year 2A"> Year 2A<br>
                <input type="checkbox" name="class" value="Year 2B"> Year 2B<br>
                <input type="checkbox" name="class" value="Year 2C"> Year 2C<br>
                <input type="checkbox" name="class" value="Year 2D"> Year 2D<br>
                <input type="checkbox" name="class" value="Year 3A"> Year 3A<br>
                <input type="checkbox" name="class" value="Year 3B"> Year 3B<br>
                <input type="checkbox" name="class" value="Year 3C"> Year 3C<br>
                <input type="checkbox" name="class" value="Year 3D"> Year 3D<br>
            </div>

            <div id="student-class">
                <label>Select Class (For Students):</label>
                <select name="class">
                    <option value="Year 1A">Year 1A</option>
                    <option value="Year 1B">Year 1B</option>
                    <option value="Year 1C">Year 1C</option>
                    <option value="Year 2A">Year 2A</option>
                    <option value="Year 2B">Year 2B</option>
                    <option value="Year 2C">Year 2C</option>
                    <option value="Year 2D">Year 2D</option>
                    <option value="Year 3A">Year 3A</option>
                    <option value="Year 3B">Year 3B</option>
                    <option value="Year 3C">Year 3C</option>
                    <option value="Year 3D">Year 3D</option>
                </select>
            </div>

            <button class="button" type="submit">Add User</button>
        </form>
    </div>
</div>
</body>
</html>
