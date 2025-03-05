package com.app.submision.submission.controller;

import com.app.submision.submission.model.Role;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the username and password from the login form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Open Hibernate session and check the user credentials
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);

        User user = null;
        try {
            user = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (user != null) {
            // Check if the entered password matches the hashed password in the database
            if (BCrypt.checkpw(password, user.getPassword())) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("user", user);

                // Print the user's role for troubleshooting
                System.out.println("Logged in user role: " + user.getRole());

                // Check the user's role and redirect to the appropriate page
                if (user.getRole().compareTo(Role.valueOf("STUDENT")) == 0){
                    response.sendRedirect("StudentServlet");
                } else if (user.getRole().compareTo(Role.valueOf("TEACHER")) == 0) {
                    response.sendRedirect("TeacherServlet");
                } else if (user.getRole().compareTo(Role.valueOf("ADMIN")) == 0) {
                    response.sendRedirect("AdminServlet");
                } else {
                    response.sendRedirect("login.jsp?error=invalid_role");
                }
            } else {
                // If the password doesn't match, redirect to login page with error
                response.sendRedirect("login.jsp?error=invalid_credentials");
            }
        } else {
            // If user is not found, redirect to login page with error
            response.sendRedirect("login.jsp?error=invalid_credentials");
        }
    }
}
