package com.example.collaboratio.controller;

import com.example.collaboratio.CollaboratioApplication;
import com.example.collaboratio.logic.DbConnector;
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

// I NEED TO IMPORT ENVIRONMENT VARIABLES FOR DB-CREDENTIALS, Its not working with ENVS right now :-(


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();

        return "redirect:/login-page";
    }

    @GetMapping("/registerNewUser")
    public String loginMock(){
        return "registerUser";
    }

    @GetMapping("/afterRegister")
    public String afterRegister(){

        return "register-submit";
    }

    @PostMapping("/insertNewUser")
    public String InsertedUser(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {
        NewUser newuser = new NewUser(username,password);

        if (newuser.userName.isEmpty() || newuser.userPassword.isEmpty()) {
            return "redirect:/registerUser";
        }
        Queries insertUser = new Queries();
        insertUser.insertNewUser(DbConnector.connection,newuser);
        //System.out.println(RequestContextHolder.getRequestAttributes().getSessionId());
        return "redirect:/afterRegister";
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
        if ( ShowRows.loginCheck(DbConnector.connection,username,password,session).equals("success") ) {
            //return "mylobby";
            return "redirect:/lobby";
        } else
            return "redirect:/login-page";
    }

    @GetMapping("lobby")
    public ModelAndView mylobby(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();



        if(myquery.checkSessionID(mycookie, DbConnector.connection)){

            MyLobbyClass lobbymodel = new MyLobbyClass();
            lobbymodel.setUserName(myquery.getUserName(mycookie,DbConnector.connection));
            lobbymodel.setUserId(myquery.getUserId(mycookie,DbConnector.connection));

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
        if(myquery.checkSessionID(mycookie, DbConnector.connection)){
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

        if(insertSession.checkSessionID(mycookie, DbConnector.connection)) {

            SessionCreation newSession = new SessionCreation(topic, problem, hints, uploadAsBlob, sessionMembers);

            insertSession.insertSession(DbConnector.connection, newSession, mycookie,uploadAsBlob);

            return "redirect:/sessions";
        }
        return "redirect:login-page";
    }





    @GetMapping("/inside-session")
    public String insideSession(@CookieValue ("JSESSIONID") String mycookie) throws SQLException {
        Queries myquery = new Queries();
        if(myquery.checkSessionID(mycookie, DbConnector.connection)) {
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
        if (myquery.checkSessionID(mycookie,DbConnector.connection)) {

            ModelAndView myview = new ModelAndView("sessions");
            return myview;
        }
        return null;
    }
}
