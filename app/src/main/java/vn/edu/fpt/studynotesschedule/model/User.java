package vn.edu.fpt.studynotesschedule.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String surname;
    private String university;
    private String username;
    private String password;

    public User() {}

    public User(String name, String surname, String university, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.university = university;
        this.username = username;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUniversity() {
        return university;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User setUserData(User currentUser, List<String> data){
        currentUser.setId(data.get(0));
        currentUser.setName(data.get(1));
        currentUser.setSurname(data.get(2));
        currentUser.setUniversity(data.get(3));
        currentUser.setUsername(data.get(4));
        currentUser.setPassword(data.get(5));
        return currentUser;
    }
}
