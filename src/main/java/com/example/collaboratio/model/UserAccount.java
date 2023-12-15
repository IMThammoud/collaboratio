package com.example.collaboratio.model;

public class UserAccount {
    private int id;
    private String user_name;
    private String token;
    private String email;
    private String avatar;
    public UserAccount(String user_name,
                       String user_password,
                       String email,
                       String img){

        this.user_name = user_name;
        this.token = user_password;
        this.email = email;
        this.avatar = img;

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
