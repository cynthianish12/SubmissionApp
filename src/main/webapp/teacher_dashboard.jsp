<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("user") == null) {
        response.sendRedirect("login.jsp?error=session_expired");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            display: flex;
        }

        header {
            background-color: #05064a;
            color: white;
            padding: 15px;
            text-align: center;
            width: 200px;
        }

        h2 {
            margin-top: 0;
        }


        .sidebar a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px 20px;
            font-size: 18px;
        }

        .sidebar a:hover {
            background-color: #0056b3;
        }

        .main-content {
            margin-left: 220px; /* Adjust to match sidebar width */
            padding: 20px;
            width: 100%;
            background-color: white;
            box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
        }

        h3 {
            color: #333;
        }

        form {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        form label {
            font-weight: bold;
            margin-bottom: 5px;
        }

        form input[type="text"], form input[type="datetime-local"], form textarea, form select {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        form button {
            background-color: #05064a;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        form button:hover {
            background-color: #05064a;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #05064a;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .submission-file {
            color: #007bff;
            text-decoration: none;
        }

        .submission-file:hover {
            text-decoration: underline;
        }

        .empty-message {
            font-style: italic;
            color: #888;
        }

        .logout-link {
            display: inline-block;
            margin-top: 15px;
            font-weight: bold;
            text-decoration: none;
            color: #f44336;
        }

        .logout-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<header>
    <h2>Teacher Dashboard</h2>
    <a class="logout-link" href="logout">Logout</a>
</header>

<div class="main-content">
    <div id="create-assignment">
        <h3>Create New Assignment</h3>
        <form action="TeacherServlet" method="post">
            <label for="title">Assignment Title:</label><br>
            <input type="text" id="title" name="title" required><br><br>

            <label for="description">Description:</label><br>
            <textarea id="description" name="description"></textarea><br><br>

            <label for="courseId">Select Course:</label><br>
            <select id="courseId" name="courseId">
                <c:forEach var="course" items="${courses}">
                    <option value="${course.id}">${course.name}</option>
                </c:forEach>
            </select><br><br>

            <label for="forStudent">Assign To (Student):</label><br>
            <select id="forStudent" name="forStudent">
                <c:forEach var="student" items="${students}">
                    <option value="${student.username}">${student.username}</option>
                </c:forEach>
            </select><br><br>

            <label for="deadline">Deadline:</label><br>
            <input type="datetime-local" id="deadline" name="deadline" required><br><br>

            <button type="submit">Create Assignment</button>
        </form>
    </div>

    <div id="manage-assignments">
        <h3>Manage Assignments</h3>
        <ul>
            <c:if test="${not empty assignments}">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>TITLE</th>
                        <th>DESCRIPTION</th>
                        <th>COURSE</th>
                        <th>DEADLINE</th>
                    </tr>
                    <c:forEach var="assignment" items="${assignments}">
                        <tr>
                            <td><c:out value="${assignment.id}"/></td>
                            <td><c:out value="${assignment.title}"/></td>
                            <td><c:out value="${assignment.description}"/></td>
                            <td>
                                <c:forEach var="course" items="${courses}">
                                    <c:if test="${course.id == assignment.course.id}">
                                        <c:out value="${course.name}"/>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td><c:out value="${assignment.deadline}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <c:if test="${empty assignments}">
                <li class="empty-message">No assignments created yet.</li>
            </c:if>
        </ul>
    </div>

    <div id="manage-submissions">
        <h3>Manage Submissions</h3>
        <c:if test="${not empty assignments}">
            <table>
                <tr>
                    <th>Assignment ID</th>
                    <th>Student Username</th>
                    <th>Submission Time</th>
                    <th>File</th>
                </tr>
                <c:forEach var="assignment" items="${assignments}">
                    <c:forEach var="submission" items="${submissions}">
                        <c:if test="${submission.assignment.id == assignment.id}">
                            <tr>
                                <td><c:out value="${assignment.id}"/></td>
                                <td><c:out value="${submission.student.username}"/></td>
                                <td><c:out value="${submission.submissionTime}"/></td>
                                <td><a class="submission-file" href="${submission.filePath}" target="_blank">View File</a></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${empty submissions}">
            <li class="empty-message">No submissions available.</li>
        </c:if>
    </div>
</div>

</body>
</html>
