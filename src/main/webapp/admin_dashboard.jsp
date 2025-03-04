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
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="sidebar">
    <div class="logo">
        <img src="logo.png" alt="Logo">
    </div>
    <ul>
        <li class="active"><i class="fas fa-home"></i> <span>Dashboard</span></li>
        <li><a href="add-user.jsp"><i class="fas fa-user-plus"></i> <span>Add User</span></a></li>
        <li><a href="add-course.jsp"><i class="fas fa-book"></i> <span>Add Course</span></a></li>
        <li><i class="fas fa-sign-out-alt"></i> <a href="logout">Logout</a></li>
    </ul>
</div>

<div class="main-content">
    <div class="content-box">
        <h2>Admin Dashboard</h2>
        <p>Welcome to the admin panel. Use the sidebar to navigate.</p>
    </div>
</div>
</body>
</html>
