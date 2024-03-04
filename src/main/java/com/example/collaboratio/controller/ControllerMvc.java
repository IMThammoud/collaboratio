package com.example.collaboratio.controller;

import com.example.collaboratio.logic.Queries;
import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.Session;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.View;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.lang.System;
@Configuration
@EnableJdbcHttpSession
@Controller
public class ControllerMvc {

// I NEED TO IMPORT ENVIRONMENT VARIABLES FOR DB-CREDENTIALS, this does not work
    String mariadb_user = System.getenv("MARIADB_USER_NAME");

    String mariadb_password = System.getenv("MARIADB_PASSWORD");


    Connection connection;

    //{
    //    try {
    //        connection = DriverManager.getConnection(
    //                "jdbc:mariadb://localhost:3306/collaboratio",
    //                mariadb_user,"gurken123");
    //    } catch (SQLException e) {
    //        throw new RuntimeException(e);
    //    }
    //}


    @GetMapping("/")
    public String welcome(HttpSession session){
        System.out.println(session.getServletContext());
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
    public String loginPage(){
        return "login-page";
    }

    @PostMapping("login")
    public String login(@RequestParam("userName") String username,
                        @RequestParam("password") String password) throws SQLException {
        Queries ShowRows = new Queries();
        if ( ShowRows.loginCheck(connection,username,password).equals("success") ) {
            return "mylobby";
        } else
            return "login-page";
    }

    @GetMapping("/session-creation")
    public String sessionCreation(){
        return "session-creation";
    }

    @PostMapping("/session-created-done")
    public String sessionCreation(@RequestParam("topic") String topic,
                                  @RequestParam("problem") String problem,
                                  @RequestParam("hints") String hints,
                                  @RequestParam("media") String media,
                                  @RequestParam("sessionmembers") int sessionMembers) throws SQLException {
        SessionCreation newSession = new SessionCreation(topic, problem, hints, media, sessionMembers);
        Queries insertSession = new Queries();
        insertSession.insertSession(connection, newSession);
        return "session-created-done";
    }

    @GetMapping("/mysessions")
    public String mySessions(){
        return "mysessions";
    }


    @GetMapping("/inside-session")
    public String insideSession(){
        return "inside-session";
    }
}
