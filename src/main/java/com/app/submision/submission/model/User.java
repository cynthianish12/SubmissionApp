package com.app.submision.submission.model;

import javax.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
//package com.app.submision.submission.model;

import org.mindrot.jbcrypt.BCrypt;
//import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "classroom_id")  // Foreign key for Classroom
    private Classroom classroom; // Reference to the Classroom entity

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // STUDENT or INSTRUCTOR

    private String salt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean checkPassword(String candidatePassword) {
        return BCrypt.checkpw(candidatePassword, this.password);
    }
}
