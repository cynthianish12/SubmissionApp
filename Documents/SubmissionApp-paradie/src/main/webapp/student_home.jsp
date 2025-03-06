<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <style>
        /* Basic s tyling for the page */
        body {
            font-family: Arial, sans-serif;
            display: flex;
            margin: 0;
            height: 100vh;
            background-color: #f4f4f9;
        }

        /* Sidebar styling */
        .sidebar {
            width: 250px;
            background-color: #1E3A8A; /* Blue color */
            color: white;
            padding: 20px;
            position: fixed;
            top: 0;
            left: 0;
            height: 100%;
            box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
        }

        .sidebar h2 {
            margin-top: 0;
            font-size: 24px;
            color: #ffffff;
        }

        .sidebar a {
            color: white;
            text-decoration: none;
            padding: 12px 0;
            display: block;
            font-size: 16px;
            margin: 8px 0;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        .sidebar a:hover {
            background-color: #3B82F6; /* Lighter blue on hover */
        }

        /* Main content area */
        .main-content {
            margin-left: 250px;
            padding: 20px;
            width: 100%;
            overflow-y: auto;
        }

        h2 {
            color: #333;
        }

        h3 {
            color: #05064a;
        }

        .dashboard-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 10px;
            margin-bottom: 30px;
        }

        label {
            font-size: 14px;
            margin-bottom: 5px;
        }

        input[type="text"], input[type="file"], select {
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 100%;
        }

        button {
            padding: 10px;
            font-size: 16px;
            background-color: #1E3A8A; /* Matching blue color */
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            background-color: #3B82F6; /* Lighter blue on hover */
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #1E3A8A;
            color: white;
        }

        a {
            color: #1E3A8A;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .no-assignments, .no-submissions {
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
<!-- Sidebar -->
<div class="sidebar">
    <h2>Student Dashboard</h2>
    <a href="#submit-assignment">Submit Assignment</a>
    <a href="#available-assignments">Available Assignments</a>
    <a href="#your-submissions">Your Submissions</a>
    <a href="logout">Logout</a>
</div>

<!-- Main content -->
<div class="main-content">
    <div id="submit-assignment" class="dashboard-container">
        <h3>Submit Assignment</h3>
        <form action="StudentServlet" method="POST" enctype="multipart/form-data">
            <label for="assignment">Select Assignment:</label>
            <select name="assignment" id="assignment" required>
                <c:forEach var="assignment" items="${assignments}">
                    <option value="${assignment.id}"><c:out value="${assignment.title}"/></option>
                </c:forEach>
            </select>

            <label for="studentId">Student ID:</label>
            <input type="text" name="studentId" id="studentId" required>

            <label for="file">Select File:</label>
            <input type="file" name="file" id="file" required>

            <button type="submit">Submit</button>
        </form>
    </div>

    <div id="available-assignments" class="dashboard-container">
        <h3>Available Assignments</h3>
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
                        <td><c:out value="${assignment.course.name}"/></td>
                        <td><c:out value="${assignment.deadline}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${empty assignments}">
            <p class="no-assignments">No assignments created yet.</p>
        </c:if>
    </div>

    <div id="your-submissions" class="dashboard-container">
        <h3>Your Previous Submissions</h3>
        <c:if test="${not empty submissions}">
            <table>
                <tr>
                    <th>Assignment Title</th>
                    <th>Submission Date</th>
                    <th>File Name</th>
                    <th>Status</th>
                </tr>
                <c:forEach var="submission" items="${submissions}">
                    <tr>
                        <td><c:out value="${submission.assignment.title}"/></td>
                        <td><c:out value="${submission.submissionTime}"/></td>
                        <td><a href="${submission.filePath}" target="_blank">View File</a></td>
                        <td>
                            <c:choose>
                                <c:when test="${submission.submissionTime != null}">Submitted</c:when>
                                <c:otherwise>Not Submitted</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${empty submissions}">
            <p class="no-submissions">You haven't submitted any assignments yet.</p>
        </c:if>
    </div>
</div>
</body>
</html>
