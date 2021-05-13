package com.example.courseprog;

public class People {

    private String id, loginn, telepone, fullName, email, sheld;

    People() {}

    People(String id, String login, String email, String fullName, String telepone) {
        this.id = id;
        this.loginn = login;
        this.telepone = telepone;
        this.fullName = fullName;
        this.email = email;
        this.sheld = "user";
    }

    public String getLoginn() {
        return loginn;
    }

    public void setLoginn(String loginn) {
        this.loginn = loginn;
    }

    public String getTelepone() {
        return telepone;
    }

    public void setTelepone(String telepone) {
        this.telepone = telepone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSheld() {
        return sheld;
    }

    public void setSheld(String sheld) {
        this.sheld = sheld;
    }
}
