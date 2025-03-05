package com.app.submision.submission.controller;

import com.app.submision.submission.model.Role;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    // Handle GET request to display login page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response); // Forward to login.jsp
    }

    // Handle POST request to process login form
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the username and password from the login form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Open Hibernate session and check the user credentials
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        User user = null;
        try {
            user = query.uniqueResult();

            // Check if the user is the first admin (if no admin exists)
            if (user == null) {
                // Check if the admin user exists
                Query<User> adminQuery = session.createQuery("FROM User WHERE role = :role", User.class);
                adminQuery.setParameter("role", Role.ADMIN);
                user = adminQuery.uniqueResult();

                if (user == null) {
                    // If no admin exists, create one
                    user = new User();
                    user.setUsername("admin");  // Set default username for admin
                    user.setPassword("admin123"); // Set default password for admin
                    user.setRole(Role.ADMIN);  // Set role as ADMIN

                    // Save the new admin to the database
                    session.beginTransaction();
                    session.save(user);
                    session.getTransaction().commit();
                    System.out.println("Default admin created.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        // If user is found, store the user in the session and redirect to the appropriate page
        if (user != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", user);

            // Print the user's role for troubleshooting
            System.out.println("Logged in user role: " + user.getRole());

            // Check the user's role and redirect to the appropriate page
            if (user.getRole().compareTo(Role.valueOf("STUDENT")) == 0){
                // Redirect to student-specific page (e.g., student_home.jsp for students)
                response.sendRedirect("StudentServlet");
            } else if (user.getRole().compareTo(Role.valueOf("TEACHER")) == 0) {
                // Redirect to teacher-specific page (e.g., teacher_dashboard.jsp for teachers)
                response.sendRedirect("TeacherServlet");
            } else if (user.getRole().compareTo(Role.valueOf("ADMIN")) == 0) {
                // Redirect to admin-specific page (e.g., admin_dashboard.jsp for admins)
                response.sendRedirect("AdminServlet");
            } else {
                // In case the role is not recognized
                response.sendRedirect("login.jsp?error=invalid_role");
            }
        } else {
            // If user is not found, redirect to login page with error
            response.sendRedirect("login.jsp?error=invalid_credentials");
        }
    }
}
