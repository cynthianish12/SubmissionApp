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
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/login")
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
        User user = null;

        try {
            // Check if the user exists in the database
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            user = query.uniqueResult();

            // If user is found and password is correct
            if (user != null && user.checkPassword(password)) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("user", user);

                // Redirect based on the user's role
                if (user.getRole().compareTo(Role.STUDENT) == 0) {
                    response.sendRedirect("StudentServlet");
                } else if (user.getRole().compareTo(Role.TEACHER) == 0) {
                    response.sendRedirect("TeacherServlet");
                } else if (user.getRole().compareTo(Role.ADMIN) == 0) {
                    response.sendRedirect("AdminServlet");
                } else {
                    response.sendRedirect("login.jsp?error=invalid_role");
                }
            } else {
                response.sendRedirect("login.jsp?error=invalid_credentials");
            }

            // Check if the user is the first admin (if no admin exists)
            if (user == null) {
                // Check if an admin user exists
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
    }
}
