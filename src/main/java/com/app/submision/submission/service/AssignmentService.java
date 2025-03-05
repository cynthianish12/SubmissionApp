package com.app.submision.submission.service;

import com.app.submision.submission.model.Assignment;
import com.app.submision.submission.model.Classroom;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class AssignmentService {

    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static AssignmentService instance;

    // Singleton pattern
    public static synchronized AssignmentService getInstance() {
        if (instance == null) {
            instance = new AssignmentService();
        }
        return instance;
    }

    // Method to add an assignment
    public void addAssignment(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(assignment);  // Persist the assignment to the database
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving assignment", e);
        }
    }

    // Method to get all assignments by instructor
    public List<Assignment> getAllAssignmentsByInstructor(User instructor) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("FROM Assignment a WHERE a.instructor = :instructor", Assignment.class);
            query.setParameter("instructor", instructor);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching assignments for instructor", e);
        }
    }

    // Method to get assignments by classroom
    public List<Assignment> getAssignmentsByClassroom(Classroom classroom) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("FROM Assignment a WHERE a.classroom = :classroom", Assignment.class);
            query.setParameter("classroom", classroom);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching assignments for classroom", e);
        }
    }

    // Method to create an assignment for a specific classroom
    public void createAssignmentForClassroom(Assignment assignment, Classroom classroom) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            assignment.setClassroom(classroom);  // Set the classroom for the assignment
            session.persist(assignment);  // Persist the assignment to the database
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating assignment for classroom", e);
        }
    }
}
