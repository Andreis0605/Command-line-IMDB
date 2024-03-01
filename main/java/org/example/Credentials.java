package org.example;

public class Credentials {
    private String email;
    private String password;

    public Credentials()
    {
        this.email = "";
        this.password = "";
    }
    public Credentials(String email, String pass) {
        this.email = email;
        this.password = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean matchingCredentials(String email, String password)
    {
        return (this.email.equals(email) && this.password.equals(password));
    }

}
