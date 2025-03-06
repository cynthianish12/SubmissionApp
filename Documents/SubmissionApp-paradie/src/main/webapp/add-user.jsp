<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add User</title>
    <link rel="stylesheet" href="styles.css">
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
        <form action="AdminServlet" method="post">
            <label>Username:</label>
            <input type="text" name="username" required>

            <label>Password:</label>
            <input type="password" name="password" required>

            <label>Role:</label>
            <select name="role" required>
                <option value="STUDENT">Student</option>
                <option value="TEACHER">Teacher</option>
            </select>

            <button class="button" type="submit">Add User</button>
        </form>
    </div>
</div>
</body>
</html>
