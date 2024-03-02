package com.example.collaboratio.controller;

import com.example.collaboratio.logic.Queries;
import com.example.collaboratio.model.NewUser;
import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.HttpCookie;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.lang.System;

@Controller
public class ControllerMvc implements ErrorController {

// I NEED TO IMPORT ENVIRONMENT VARIABLES FOR DB-CREDENTIALS, this does not work
    String mariadb_user = "trondl";
    String mariadb_password = "bedepe";
    Connection connection;

    {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/logindata",
                    "trondl",
                    "bedepe");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/insertUser")
    public String loginMock(){
        return "registerUser";
    }

    @PostMapping("/insertNewUser")
    public String InsertedUser(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {
        NewUser newuser = new NewUser(username,password);
        Queries insertUser = new Queries();
        insertUser.insertNewUser(connection,newuser);
        //System.out.println(RequestContextHolder.getRequestAttributes().getSessionId());
        return "register-submit";
    }

    @GetMapping("/")
    public String welcome(){
        return "index";
    }

    @GetMapping("/register")
    public String register(@RequestAttribute("SessionID") HttpSession session){

        //return "register";
        return session.getId();
    }

    @PostMapping("/register-submit")
    public String registerSubmit(@RequestParam("userName") String userName,
                                 @RequestParam("token") String token,
                                 @RequestParam("email") String email,
                                 @RequestParam("avatar") String avatarChoice) throws SQLException {
        UserAccount newUser = new UserAccount(userName,token,email,avatarChoice);
        System.out.println(newUser.getEmail());

        Queries insertUser = new Queries();
        // If some attribute of newUser is empty (so an empty param is passed) no SQL query will be made
        if (newUser.getUser_name().isEmpty() || newUser.getToken().isEmpty() || newUser.getEmail().isEmpty() || newUser.getAvatar().isEmpty()) {
            return "register";
        } else
            insertUser.insertUserAndSessionToken(connection,newUser);
        // create new user_account with request-parameters
        // check if parameters are valid -> if not return error page
        // store Attributes of new user_account in Database
        return "register-submit";
    }

    @GetMapping("/login-page")
    public String loginPage(HttpSession session){

        // Session will become invalidated if already logged in user goes and hits the login-page endpoint
        System.out.println("SessionID before invalidating" + session.getId());
        session.invalidate();

        System.out.println("SessionID after invalidating" + session.getId());
        return "login-page";
    }

    @PostMapping("login")
    public String login(@RequestParam("userName") String username,
                        @RequestParam("password") String password,
                        HttpSession session) throws SQLException {

        // Stores the created SessionID in a String
        String currentSession = session.getId();
        System.out.println(currentSession);


        Queries ShowRows = new Queries();
        if ( ShowRows.loginCheck(connection,username,password,session).equals("success") ) {
            return "mylobby";
        } else
            return "login-page";
    }

    @GetMapping("/session-creation")
    public String sessionCreation(@CookieValue ("JSESSIONID") String mycookie){
        return "session-creation";
    }

    @PostMapping("/session-created-done")
    public String sessionCreation(@RequestParam("topic") String topic,
                                  @RequestParam("problem") String problem,
                                  @RequestParam("hints") String hints,
                                  @RequestParam("media") String media,
                                  @RequestParam("sessionmembers") int sessionMembers,
                                  @CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        SessionCreation newSession = new SessionCreation(topic, problem, hints, media, sessionMembers);
        Queries insertSession = new Queries();
        insertSession.insertSession(connection, newSession, mycookie);
        return "session-created-done";
    }

    @GetMapping("/mysessions")
    public String mySessions(@CookieValue ("JSESSIONID") String mycookie){
        return "mysessions";
    }


    @GetMapping("/inside-session")
    public String insideSession(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if(myquery.checkSessionID(mycookie, connection)) {
            System.out.println(mycookie);
            return "inside-session";
        }else return "login-page";

    }

    @GetMapping("/error")
    public String errorMessage(){
        return "returnToHome";
    }
}
