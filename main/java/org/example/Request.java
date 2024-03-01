package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Request implements Subject {

    private List<Observer> observers = new ArrayList<>();

    private RequestType requestType;

    private LocalDateTime createdDate;

    private String problemName;

    private String desctiption;

    private String username;

    private String to;


    public Request() {
        this.requestType = null;
        this.createdDate = LocalDateTime.now();
        this.problemName = null;
        this.desctiption = null;
        this.username = null;
        this.to = null;
    }

    public Request(RequestType requestType, LocalDateTime createdDate, String problemName, String desctiption, String username, String to) {
        this.requestType = requestType;
        this.createdDate = createdDate;
        this.problemName = problemName;
        this.desctiption = desctiption;
        this.username = username;
        this.to = to;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String toString() {
        String aux = "";
        aux += "Type: " + this.requestType.name() + "\n";
        aux += "Creation date: " + this.createdDate.toString() + "\n";
        aux += "Created by: " + this.username + "\n";
        if (this.problemName != null) aux += "Problem: " + this.problemName + "\n";
        aux += "To: " + this.to + "\n";
        aux += "Description: " + this.desctiption + "\n";
        return aux;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String notification, String destination) {
        if (destination.equals("emitter")) {
            for (Observer auxObserver : observers) {
                if (((User) auxObserver).getUserType() == AccountType.Regular) {
                    auxObserver.update(notification);
                }
            }
        } else {
            for (Observer auxObserver : observers) {
                if (((User) auxObserver).getUserType() != AccountType.Regular) {
                    auxObserver.update(notification);
                }
            }
        }

    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }
}
