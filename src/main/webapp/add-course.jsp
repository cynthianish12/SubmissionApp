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

            <label>Assign to Class:</label>
            <select name="classSelection" required>
                <option value="YEAR_ONE_A">Year One A</option>
                <option value="YEAR_ONE_B">Year One B</option>
                <option value="YEAR_ONE_C">Year One C</option>
                <option value="YEAR_TWO_A">Year Two A</option>
                <option value="YEAR_TWO_B">Year Two B</option>
                <option value="YEAR_TWO_C">Year Two C</option>
                <option value="YEAR_TWO_D">Year Two D</option>
                <option value="YEAR_THREE_A">Year Three A</option>
                <option value="YEAR_THREE_B">Year Three B</option>
                <option value="YEAR_THREE_C">Year Three C</option>
                <option value="YEAR_THREE_D">Year Three D</option>
            </select>

            <label>Assign Instructor:</label>
            <select name="instructorId" required>
                <option value="1">Instructor 1</option>
                <option value="2">Instructor 2</option>
                <option value="3">Instructor 3</option>
                <option value="4">Instructor 4</option>
                <option value="5">Instructor 5</option>
                <option value="6">Instructor 6</option>
                <option value="7">Instructor 7</option>
                <option value="8">Instructor 8</option>
                <option value="9">Instructor 9</option>
                <option value="10">Instructor 10</option>
                <option value="11">Instructor 11</option>
                <option value="12">Instructor 12</option>
                <option value="13">Instructor 13</option>
                <option value="14">Instructor 14</option>
                <option value="15">Instructor 15</option>
                <option value="16">Instructor 16</option>
            </select>

            <button class="button" type="submit">Add Course</button>
        </form>
    </div>
</div>
</body>
</html>
