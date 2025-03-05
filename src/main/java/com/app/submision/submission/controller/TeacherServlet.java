package com.app.submision.submission.controller;
import com.app.submision.submission.model.*;
import com.app.submision.submission.service.AssignmentService;
import com.app.submision.submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;
public class TeacherServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        User instructor = (User) session.getAttribute("user");
        AssignmentService assignmentService = AssignmentService.getInstance();
        List<Assignment> assignments = assignmentService.getAllAssignmentsByInstructor(instructor);

        // Fetch all submissions from the database
        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();
        }

        // Troubleshooting: Log the submission list size or null check
        if (allSubmissions != null && !allSubmissions.isEmpty()) {
            System.out.println("All submissions found: ");
            for (Submission submission : allSubmissions) {
                System.out.println("Submission ID: " + submission.getId() +
                        ", Assignment Title: " + submission.getAssignment().getTitle() +
                        ", Student Name: " + submission.getStudent().getUsername() +
                        ", File Path: " + submission.getFilePath() +
                        ", Submission Time: " + submission.getSubmissionTime());
            }
        } else {
            System.out.println("No submissions found.");
        }

        // Fetch all courses from the database
        List<Course> courses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            courses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        // Fetch the Classroom enum values and their class names
        Classroom[] classrooms = Classroom.values();
        String[] classroomNames = new String[classrooms.length];
        for (int i = 0; i < classrooms.length; i++) {
            classroomNames[i] = classrooms[i].getClassName();  // Get the name of each classroom
        }

        // Pass assignments, courses, submissions, and classroom names to the JSP
        request.setAttribute("assignments", assignments);
        request.setAttribute("courses", courses);
        request.setAttribute("submissions", allSubmissions);
        request.setAttribute("classrooms", classroomNames); // Pass the names of the classrooms
        request.getRequestDispatcher("teacher_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session and ensure the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        // Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String classroomStr = request.getParameter("classroom"); // Get the classroom enum value from the form
        LocalDateTime deadline = LocalDateTime.parse(request.getParameter("deadline"));

        // Get the logged-in user (instructor)
        User instructor = (User) session.getAttribute("user");

        // Log courseId and classroom for debugging
        System.out.println("Course ID received: " + courseId);
        System.out.println("Classroom received: " + classroomStr);

        // Get the SessionFactory and open a session
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            hibernateSession.beginTransaction();

            // Get the course based on the courseId
            Course course = hibernateSession.get(Course.class, courseId);
            if (course == null) {
                // Log if course is not found
                System.out.println("Course not found with ID: " + courseId);

                // If course does not exist, send an error response
                response.sendRedirect("error.jsp?message=Course%20not%20found");
                return;
            }

            // Log if course was found successfully
            System.out.println("Course found: " + course.getName());

            // Convert classroomStr to the appropriate Classroom enum
            Classroom classroom = Classroom.valueOf(classroomStr.toUpperCase());
            if (classroom == null) {
                // Log if classroom is invalid
                System.out.println("Invalid classroom: " + classroomStr);

                // If classroom is invalid, send an error response
                response.sendRedirect("error.jsp?message=Invalid%20classroom");
                return;
            }

            // Log if classroom was found successfully
            System.out.println("Classroom selected: " + classroom.getClassName());

            // Create a new Assignment object
            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);
            assignment.setInstructor(instructor);
            assignment.setCourse(course);
            assignment.setClassroom(classroom); // Set the classroom using the enum value

            // Save the new assignment to the database
            hibernateSession.save(assignment);

            // Commit the transaction
            hibernateSession.getTransaction().commit();

            // Redirect back to the teacher dashboard with success message
            response.sendRedirect("TeacherServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Error%20while%20creating%20assignment");
        }
    }
}
