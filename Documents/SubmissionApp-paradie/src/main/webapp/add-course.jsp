<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Course</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="sidebar">
    <div class="logo">
        <img src="logo.png" alt="Logo">
    </div>
    <ul>
        <li><a href="admin-dashboard.jsp"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>
        <li><a href="add-user.jsp"><i class="fas fa-user-plus"></i> <span>Add User</span></a></li>
        <li class="active"><i class="fas fa-book"></i> <span>Add Course</span></li>
        <li><i class="fas fa-sign-out-alt"></i> <a href="logout">Logout</a></li>
    </ul>
</div>

<div class="main-content">
    <div class="content-box">
        <h2>Add New Course</h2>
        <form action="AdminServlet" method="post">
            <label>Course Name:</label>
            <input type="text" name="courseName" required>

            <label>Course Description:</label>
            <textarea name="courseDescription" required></textarea>

            <label>Instructor ID:</label>
            <input type="text" name="instructorId" required>

            <button class="button" type="submit">Add Course</button>
        </form>
    </div>
</div>
</body>
</html>
