package com.example.collaboratio.model;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;

public class SessionCreation {
    private int sessionId;
    private int sessionHost;
    private String sessionTopic;
    private String sessionProblem;
    private String sessionHints;
    private Blob media;
    private int sessionMembers;
    private String inputHost;
    private String inputMemberTwo;
    private String inputMemberThree;
    private String inputMemberFour;
    public SessionCreation(String sessionTopic,
                           String sessionProblem,
                           String sessionHints,
                           Blob media,
                           int sessionMembers){
        this.sessionTopic = sessionTopic;
        this.sessionProblem = sessionProblem;
        this.sessionHints = sessionHints;
        this.media = media;
        this.sessionMembers = sessionMembers;
        this.sessionId = sessionId;

    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionHost() {
        return sessionHost;
    }

    public void setSessionHost(int sessionHost) {
        this.sessionHost = sessionHost;
    }

    public String getSessionTopic() {
        return sessionTopic;
    }

    public void setSessionTopic(String sessionTopic) {
        this.sessionTopic = sessionTopic;
    }

    public String getSessionProblem() {
        return sessionProblem;
    }

    public void setSessionProblem(String sessionProblem) {
        this.sessionProblem = sessionProblem;
    }

    public String getSessionHints() {
        return sessionHints;
    }

    public void setSessionHints(String sessionHints) {
        this.sessionHints = sessionHints;
    }

    public Blob getMedia() {
        return media;
    }

    public void setMedia(Blob media) {
        this.media = media;
    }

    public int getSessionMembers() {
        return sessionMembers;
    }

    public void setSessionMembers(int sessionMembers) {
        this.sessionMembers = sessionMembers;
    }

    public String getInputHost() {
        return inputHost;
    }

    public void setInputHost(String inputHost) {
        this.inputHost = inputHost;
    }

    public String getInputMemberTwo() {
        return inputMemberTwo;
    }

    public void setInputMemberTwo(String inputMemberTwo) {
        this.inputMemberTwo = inputMemberTwo;
    }

    public String getInputMemberThree() {
        return inputMemberThree;
    }

    public void setInputMemberThree(String inputMemberThree) {
        this.inputMemberThree = inputMemberThree;
    }

    public String getInputMemberFour() {
        return inputMemberFour;
    }

    public void setInputMemberFour(String inputMemberFour) {
        this.inputMemberFour = inputMemberFour;
    }
}
