package com.codesolution.projectmanagement.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "project_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project", referencedColumnName = "id")
    private Project project;

    //Administrateur, Membre, Observateur
    private String role;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.equals("Administrateur") && !role.equals("Membre") && !role.equals("Observateur"))  {
            throw new IllegalArgumentException("Le rôle doit être Administrateur, Membre ou Observateur");
        }
        this.role = role;
    }


}
