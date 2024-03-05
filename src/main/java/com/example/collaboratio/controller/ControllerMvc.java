package com.example.collaboratio.controller;

import com.example.collaboratio.logic.Queries;
import com.example.collaboratio.model.MyLobbyClass;
import com.example.collaboratio.model.NewUser;
import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Decoder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.Blob;
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

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();

        return "redirect:/login-page";
    }

    @GetMapping("/registerNewUser")
    public String loginMock(){
        return "registerUser";
    }

    @PostMapping("/insertNewUser")
    public String InsertedUser(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {
        NewUser newuser = new NewUser(username,password);

        if (newuser.userName.isEmpty() || newuser.userPassword.isEmpty()) {
            return "redirect:/registerUser";
        }
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
    public String loginPage(){

        // Session will become invalidated if already logged in user goes and hits the login-page endpoint
        //System.out.println("SessionID before login" + session.getId());


        //System.out.println("SessionID after invalidating" + session.getId());
        return "login-page";
    }

    @PostMapping("login")
    public String login(@RequestParam("userName") String username,
                        @RequestParam("password") String password,
                        HttpSession session) throws SQLException {

        // Stores the created SessionID in a String
        String currentSession = session.getId();
        session.invalidate();
        System.out.println("Current session after login" + currentSession);


        Queries ShowRows = new Queries();
        if ( ShowRows.loginCheck(connection,username,password,session).equals("success") ) {
            //return "mylobby";
            return "redirect:/lobby";
        } else
            return "redirect:/login-page";
    }

    @GetMapping("lobby")
    public ModelAndView mylobby(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();



        if(myquery.checkSessionID(mycookie, connection)){

            MyLobbyClass lobbymodel = new MyLobbyClass();
            lobbymodel.setUserName(myquery.getUserName(mycookie,connection));
            lobbymodel.setUserId(myquery.getUserId(mycookie,connection));

            ModelAndView toRender = new ModelAndView("mylobby");
            toRender.addObject("user_name",lobbymodel.getUserName());
            toRender.addObject("user_id",lobbymodel.getUserId());
            return toRender;
        }
        ModelAndView returnLogin = new ModelAndView("login-page");
        return returnLogin;
    }


    @GetMapping("/session-creation")
    public String sessionCreation(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if(myquery.checkSessionID(mycookie, connection)){
            return "session-creation";
        }
        return "redirect:/login-page";
    }

    @PostMapping("/session-created-done")
    public String sessionCreation(@RequestParam("topic") String topic,
                                  @RequestParam("problem") String problem,
                                  @RequestParam("hints") String hints,
                                  @RequestParam("media") MultipartFile media,
                                  @RequestParam("sessionmembers") int sessionMembers,
                                  @CookieValue ("JSESSIONID") String mycookie) throws SQLException, IOException {

        Queries insertSession = new Queries();

        MultipartFile myfile = media;
        byte[] uploadAsBytes = myfile.getBytes();
        Blob uploadAsBlob = new SerialBlob(uploadAsBytes);

        if(insertSession.checkSessionID(mycookie, connection)) {

            SessionCreation newSession = new SessionCreation(topic, problem, hints, uploadAsBlob, sessionMembers);

            insertSession.insertSession(connection, newSession, mycookie,uploadAsBlob);

            return "session-created-done";
        }
        return "redirect:login-page";
    }

    @GetMapping("/mysessions")
    public String mySessions(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if(myquery.checkSessionID(mycookie, connection)){
            return "mysessions";
        }
        return "redirect:/login-page";
    }


    @GetMapping("/inside-session")
    public String insideSession(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if(myquery.checkSessionID(mycookie, connection)) {
            System.out.println(mycookie);
            return "inside-session";
        } return "redirect:/login-page";

    }

    @GetMapping("/error")
    public String errorMessage(){
        return "returnToHome";
    }


    @GetMapping("/sessions")
    public ModelAndView sessions(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if (myquery.checkSessionID(mycookie,connection)) {

            ModelAndView myview = new ModelAndView("sessions");
            myquery.getDataForCards(mycookie,connection);
            return myview;
        }
        return null;
    }
}
