package com.app.submision.submission;

import com.app.submision.submission.model.Role;
import com.app.submision.submission.model.User;
import com.app.submision.submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class Program {
    public static void main(String[] args) {
        // Initialize Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        // Start a new transaction
        Transaction transaction = null;

        try {
            // Start the transaction
            transaction = session.beginTransaction();

            // Create a new user object with admin credentials
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin", BCrypt.gensalt());
            adminUser.setRole(Role.ADMIN); // Set role as ADMIN

            // Save the user to the database
            session.persist(adminUser);

            // Commit the transaction
            transaction.commit();

            System.out.println("Admin user created successfully.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback the transaction in case of an error
            }
            e.printStackTrace();
        } finally {
            session.close(); // Close the session
        }
    }
}
