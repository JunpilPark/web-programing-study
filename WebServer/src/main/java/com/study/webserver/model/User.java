package com.study.webserver.model;

public class User {
    String userID;
    String password;
    String name;
    String email;

    public User(String id, String password, String name, String email) {
        this.userID = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return userID;
    }

    public void setId(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID : " + this.getId() + ", PASS : " + this.getPassword() + ", NAME : " + this.name + ", E-Mail : " + this.email;
    }
}
