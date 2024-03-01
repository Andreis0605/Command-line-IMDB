package org.example;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Actor implements Comparable, LikableItems {
    private String name;

    private HashMap<String, String> performances;

    private String biography;

    public Actor() {
        this.name = null;
        this.performances = new HashMap<String, String>();
        this.biography = null;
    }

    public Actor(String name, String biography) {
        this.name = name;
        this.performances = new HashMap<String, String>();
        this.biography = biography;
    }

    //getters

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    //setters

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void addPerformances(String name, String type) {
        this.performances.put(name, type);
    }

    public void removePerformances(String name) {
        this.performances.remove(name);
    }

    public String toString() {
        String aux = "";
        if (this.name != null) aux += "Name: " + this.name + "\n";
        if (this.biography != null) aux +="Biography: " +  this.biography + "\n";

        aux += "Performances:" + "\n";

        for (Map.Entry<String, String> entry : performances.entrySet()) {
            if (entry != null) aux += entry.getValue() + ": " + entry.getKey() + "\n";
        }
        return aux;
    }

    @Override
    public String giveName() {
        return getName();
    }

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((LikableItems) o ).giveName());
    }

    public HashMap<String, String> getPerformances() {
        return performances;
    }

    public void setPerformances(HashMap<String, String> performances) {
        this.performances = performances;
    }
}
