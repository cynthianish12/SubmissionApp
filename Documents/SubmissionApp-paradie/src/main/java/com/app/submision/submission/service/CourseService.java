package com.app.submision.submission.service;

import com.app.submision.submission.model.Course;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CourseService {
    private static CourseService instance;

    private CourseService() {}

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    public Course getCourseById(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, courseId);
        }
    }

    public List<Course> getCoursesByInstructor(User instructor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Course WHERE instructor = :instructor", Course.class)
                    .setParameter("instructor", instructor)
                    .list();
        }
    }
}
