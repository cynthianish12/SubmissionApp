<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Log In</title>
    <style>
        body {
            background-color: #9BD2CC;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
        }
        .logo {
            width: 120px;
            margin-bottom: 10px;
        }
        .title {
            color: #06083C;
            font-size: 36px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .login-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        h1 {
            color: #06083C;
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .success {
            color: green;
            font-size: 14px;
            margin-bottom: 10px;
        }
        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        label {
            font-size: 14px;
            font-weight: bold;
            margin-top: 10px;
            color: #333;
            align-self: flex-start;
        }
        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            outline: none;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }
        input:focus {
            border-color: #06083C;
            box-shadow: 0 0 5px rgba(6, 8, 60, 0.2);
        }
        button {
            background-color: #06083C;
            color: white;
            font-size: 18px;
            padding: 10px;
            border: none;
            border-radius: 5px;
            width: 100%;
            margin-top: 15px;
            cursor: pointer;
            transition: 0.3s;
        }
        button:hover {
            background-color: #092060;
        }
    </style>
</head>
<body>

<!-- Header Section -->
<div class="container">
    <img src="logo.png" alt="Logo" class="logo">
</div>

<h1>Welcome to the Submission App</h1>

<div class="login-container">
    <h2>Login</h2>

    <!-- Display error message if login fails -->
    <% String error = request.getParameter("error"); %>
    <% if (error != null) { %>
    <p class="error">
        <% if ("invalid_credentials".equals(error)) { %>
        Invalid username or password.
        <% } else if ("invalid_role".equals(error)) { %>
        User role not recognized.
        <% } else if ("session_expired".equals(error)) { %>
        Your session has expired. Please log in again.
        <% } %>
    </p>
    <% } %>

    <!-- Display logout message -->
    <% String logout = request.getParameter("logout"); %>
    <% if (logout != null && logout.equals("true")) { %>
    <p class="success">You have successfully logged out.</p>
    <% } %>

    <form action="login" method="POST">
        <label for="username">Username:</label>
        <input type="text" name="username" id="username" required>

        <label for="password">Password:</label>
        <input type="password" name="password" id="password" required>

        <button type="submit">Login</button>
    </form>
</div>

</body>
</html>
