package com.example.bookingapp.security;

public class UserInfo {
    static String token;
    static String username;
    static String role;

    public static void setToken(String token) {
        UserInfo.token = "Bearer "+token;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    public static void setRole(String role) {
        UserInfo.role = role;
    }

    static public String getToken() {
        return token;
    }

    static public String getUsername() {
        return username;
    }

    static public String getRole() {
        return role;
    }

    static public void reset(){
        setRole("");
        setUsername("");
        token="";
    }

}
