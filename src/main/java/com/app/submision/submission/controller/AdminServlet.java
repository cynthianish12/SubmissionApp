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

        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();
        }

        List<Course> allCourses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allCourses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        request.setAttribute("assignments", allAssignments);
        request.setAttribute("submissions", allSubmissions);
        request.setAttribute("courses", allCourses);
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseName = request.getParameter("courseName");
        String courseDescription = request.getParameter("courseDescription");
        String instructorName = request.getParameter("instructorName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleString = request.getParameter("role");
        String className = request.getParameter("className");

        List<String> instructors = List.of("Hatangimbabazi hilaire", "habanabashaka jean damasccene", "musaninyange mahoro larisee", "mukama louis", "mwizerwa stanley", "niyigaba emmanuel");
        List<String> classes = List.of("Y1A", "Y1B", "Y1C", "Y2A", "Y2B", "Y2C", "Y2D", "Y3A", "Y3B", "Y3C", "Y3D");

        if (courseName != null && courseDescription != null && instructorName != null) {
            if (!instructors.contains(instructorName)) {
                request.setAttribute("errorMessage", "Invalid instructor name.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                User instructor = (User) hibernateSession.createQuery("FROM User WHERE fullName = :name")
                        .setParameter("name", instructorName)
                        .uniqueResult();

                if (instructor == null || !instructor.getRole().equals(Role.TEACHER)) {
                    request.setAttribute("errorMessage", "Instructor not found or not a teacher.");
                    request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                    return;
                }

                Course newCourse = new Course();
                newCourse.setName(courseName);
                newCourse.setDescription(courseDescription);
                newCourse.setInstructorId(instructor.getId());

                Transaction tx = hibernateSession.beginTransaction();
                hibernateSession.persist(newCourse);
                tx.commit();
                request.setAttribute("successMessage", "Course added successfully.");
            }
        }

        if (username != null && password != null && roleString != null) {
            Role role = Role.valueOf(roleString);
            if (role == Role.STUDENT && (className == null || !classes.contains(className))) {
                request.setAttribute("errorMessage", "Invalid or missing class for student.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = hibernateSession.beginTransaction();

                User newUser = new User();
                newUser.setUsername(username);
                String salt = BCrypt.gensalt();
                String hashedPassword = BCrypt.hashpw(password, salt);
                newUser.setPassword(hashedPassword);

                newUser.setRole(role);

                if (role == Role.STUDENT) {
                    // Fetch the Classroom object based on className
                    Classroom classroom = (Classroom) hibernateSession.createQuery("FROM Classroom WHERE name = :className")
                            .setParameter("className", className)
                            .uniqueResult();

                    if (classroom != null) {
                        newUser.setClassroom(classroom);  // Set the Classroom object
                    } else {
                        request.setAttribute("errorMessage", "Classroom not found.");
                        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                        return;
                    }
                }

                hibernateSession.persist(newUser);
                tx.commit();
                request.setAttribute("successMessage", "User added successfully.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error adding user: " + e.getMessage());
            }
        }

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    private User getInstructorById(int instructorId) {
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            return hibernateSession.get(User.class, instructorId);
        }
    }
}
