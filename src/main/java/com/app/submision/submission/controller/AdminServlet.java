package com.app.submision.submission.controller;

import com.app.submision.submission.model.*;
import com.app.submision.submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class AdminServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        // Retrieve all assignments
        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        // Retrieve all submissions
        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();
        }

        // Retrieve all courses
        List<Course> allCourses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allCourses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        // Pass data to the JSP
        request.setAttribute("assignments", allAssignments);
        request.setAttribute("submissions", allSubmissions);
        request.setAttribute("courses", allCourses);
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseName = request.getParameter("courseName");
        String courseDescription = request.getParameter("courseDescription");
        String instructorIdString = request.getParameter("instructorId");
        String username = request.getParameter("username"); // New field for username
        String password = request.getParameter("password"); // New field for password
        String roleString = request.getParameter("role"); // New field for role

        // Handling Course Creation
        if (courseName != null && courseDescription != null && instructorIdString != null) {
            if (courseName == null || courseDescription == null || instructorIdString == null) {
                request.setAttribute("errorMessage", "Invalid input.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            try {
                int instructorId = Integer.parseInt(instructorIdString);
                User instructor = getInstructorById(instructorId);
                if (instructor == null || !instructor.getRole().equals(Role.TEACHER)) {
                    request.setAttribute("errorMessage", "Invalid instructor.");
                    request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                    return;
                }

                Course newCourse = new Course();
                newCourse.setName(courseName);
                newCourse.setDescription(courseDescription);
                newCourse.setInstructorId(instructorId);

                try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction tx = hibernateSession.beginTransaction();
                    hibernateSession.persist(newCourse);
                    tx.commit();
                    request.setAttribute("successMessage", "Course added successfully.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Instructor ID must be a valid number.");
            }
        }

        // Handling User Creation
        if (username != null && password != null && roleString != null) {
            Role role = Role.valueOf(roleString); // Convert string to Role enum

            // Check if role is valid
            if (role == null) {
                request.setAttribute("errorMessage", "Invalid role.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            try {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password, BCrypt.gensalt());  // You may want to hash the password before storing it
                newUser.setRole(role);

                try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction tx = hibernateSession.beginTransaction();
                    hibernateSession.persist(newUser);
                    tx.commit();
                    request.setAttribute("successMessage", "User added successfully.");
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error adding user: " + e.getMessage());
            }
        }

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    private User getInstructorById(int instructorId) {
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            return hibernateSession.get(User.class, instructorId); // Fetch the user by instructor ID
        }
    }
}
