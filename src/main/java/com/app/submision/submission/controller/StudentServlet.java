package com.app.submision.submission.controller;

import com.app.submision.submission.model.Assignment;
import com.app.submision.submission.model.Classroom;
import com.app.submision.submission.model.Submission;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;

public class StudentServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        String downloadFileId = request.getParameter("download");
        if (downloadFileId != null) {
            downloadFile(Long.parseLong(downloadFileId), response);
            return;
        }

        User student = (User) session.getAttribute("user");

        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        List<Submission> studentSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            studentSubmissions = hibernateSession.createQuery("FROM Submission WHERE student = :student", Submission.class)
                    .setParameter("student", student)
                    .list();
        }

        request.setAttribute("assignments", allAssignments);
        request.setAttribute("submissions", studentSubmissions);
        request.getRequestDispatcher("student_home.jsp").forward(request, response);
    }

    private void downloadFile(Long submissionId, HttpServletResponse response) throws IOException {
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            Submission submission = hibernateSession.get(Submission.class, submissionId);
            if (submission != null) {
                File file = new File(submission.getFilePath());
                if (file.exists()) {
                    response.setContentType(Files.probeContentType(file.toPath()));
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        fileInputStream.transferTo(response.getOutputStream());
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Submission not found.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        Classroom classroom = (Classroom) session.getAttribute("classroom");
        String assignmentId = request.getParameter("assignment");
        String studentId = request.getParameter("studentId");


        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);


        if (assignmentId != null && classroom != null &&studentId != null && filePart != null) {
            // Save the file on the server
            String filePath = saveFile(filePart, fileName);

            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                hibernateSession.beginTransaction();

                Assignment assignment = hibernateSession.get(Assignment.class, Integer.parseInt(assignmentId));

                User student = (User) session.getAttribute("user");

                if (assignment != null && student != null) {
                    Submission submission = new Submission();
                    submission.setAssignment(assignment);
                    submission.setStudent(student);
                    submission.setFilePath(filePath);
                    submission.setClassroom(classroom);
                    submission.setSubmissionTime(LocalDateTime.now());

                    try {
                        hibernateSession.persist(submission);
                        hibernateSession.getTransaction().commit();
                        System.out.println("Submission saved: " + submission);
                    } catch (Exception e) {
                        e.printStackTrace();
                        hibernateSession.getTransaction().rollback();  // Rollback in case of error
                        response.sendRedirect("StudentServlet?error=submission_failed");
                        return;
                    }
                } else {
                    System.out.println("Assignment or student not found.");
                    response.sendRedirect("StudentServlet?error=invalid_data");
                    return;
                }
            }

            // Redirect back to the student home with success message
            response.sendRedirect("StudentServlet?success=submission_successful");
        } else {
            response.sendRedirect("StudentServlet?error=missing_fields");
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf("=") + 2, cd.length() - 1);
            }
        }
        return null;
    }

    private String saveFile(Part filePart, String fileName) throws IOException {
        String uploadDir = getServletContext().getRealPath("/uploads");
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File file = new File(uploadDir + File.separator + fileName);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println("File saved at: " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }
}
