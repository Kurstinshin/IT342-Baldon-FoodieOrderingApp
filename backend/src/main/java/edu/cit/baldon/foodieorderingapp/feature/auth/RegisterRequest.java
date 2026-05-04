package edu.cit.baldon.foodieorderingapp.feature.auth;

public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public String getFirstname() { return firstname; }
    public void   setFirstname(String v) { this.firstname = v; }
    public String getLastname()  { return lastname; }
    public void   setLastname(String v)  { this.lastname = v; }
    public String getEmail()     { return email; }
    public void   setEmail(String v)     { this.email = v; }
    public String getPassword()  { return password; }
    public void   setPassword(String v)  { this.password = v; }
}
