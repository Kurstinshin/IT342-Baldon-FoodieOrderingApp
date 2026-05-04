package edu.cit.baldon.foodieorderingapp.feature.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
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

    public User() {}

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname  = lastname;
        this.email     = email;
        this.password  = password;
        this.role      = "CUSTOMER";
    }

    public Long   getId()        { return id; }
    public String getFirstname() { return firstname; }
    public void   setFirstname(String v) { this.firstname = v; }
    public String getLastname()  { return lastname; }
    public void   setLastname(String v)  { this.lastname = v; }
    public String getEmail()     { return email; }
    public void   setEmail(String v)     { this.email = v; }
    public String getPassword()  { return password; }
    public void   setPassword(String v)  { this.password = v; }
    public String getRole()      { return role; }
    public void   setRole(String v)      { this.role = v; }
}
