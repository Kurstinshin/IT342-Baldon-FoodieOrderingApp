package edu.cit.baldon.foodieorderingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    private String role = "CUSTOMER";

    public User(){}

    public User(String firstname, String lastname, String email, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = "CUSTOMER";
    }

    public Long getId(){ return id; }

    public String getFirstname(){ return firstname; }
    public void setFirstname(String firstname){ this.firstname=firstname; }

    public String getLastname(){ return lastname; }
    public void setLastname(String lastname){ this.lastname=lastname; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email=email; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password=password; }

    public String getRole(){ return role; }
    public void setRole(String role){ this.role=role; }
}