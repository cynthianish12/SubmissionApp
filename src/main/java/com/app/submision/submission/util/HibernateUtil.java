package com.app.submision.submission.util;

import com.app.submision.submission.model.User;
import com.app.submision.submission.model.Course;
import com.app.submision.submission.model.Assignment;
import com.app.submision.submission.model.Submission;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();

            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/submit");
            settings.put(Environment.USER, "postgres");
            settings.put(Environment.PASS, "4208");
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

            // Show SQL in console
            settings.put(Environment.SHOW_SQL, true);

            settings.put(Environment.HBM2DDL_AUTO, "create-drop");


            // Apply settings to configuration
            configuration.setProperties(settings);

            // Add annotated entity classes
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Course.class);
            configuration.addAnnotatedClass(Assignment.class);
            configuration.addAnnotatedClass(Submission.class);

            // Create ServiceRegistry
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            // Build SessionFactory
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}
