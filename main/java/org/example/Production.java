package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Production implements Comparable, LikableItems, Subject {

    public abstract void displayInfo();

    private List<Observer> observers = new ArrayList<>();
    private String title;
    private List<String> directors;

    private List<String> actors;

    private List<Genre> genres;

    private List<Rating> ratings;

    private String description;

    private double grade;


    //constructors
    public Production(String title, ArrayList<String> directors, ArrayList<String> actors, ArrayList<Genre> genres, String description) {
        this.title = title;

        this.directors = new ArrayList<String>();
        this.directors = directors;

        this.actors = new ArrayList<String>();
        this.actors = actors;

        this.genres = new ArrayList<Genre>();
        this.genres = genres;

        this.ratings = new ArrayList<Rating>();

        this.description = description;
    }

    public Production() {
        this.title = "";
        this.directors = new ArrayList<String>();
        this.actors = new ArrayList<String>();
        this.genres = new ArrayList<Genre>();
        this.ratings = new ArrayList<Rating>();
        this.description = "";
        this.grade = -1;
    }

    //getters
    public String getTitle() {
        return title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getDescription() {
        return description;
    }

    public double getGrade() {
        return grade;
    }

    //setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //adders and removers
    public void addDirector(String newDirector) {
        directors.add(newDirector);
    }

    public void removeDirector(String oldDirector) {
        if (directors.indexOf(oldDirector) != -1) {
            directors.remove(directors.indexOf(oldDirector));
        }
    }

    public void addActor(String newActor) {
        actors.add(newActor);
    }

    public void removeActor(String oldActor) {
        if (directors.indexOf(oldActor) != -1) {
            directors.remove(directors.indexOf(oldActor));
        }
    }

    public void addGenre(Genre newGenre) {
        if (genres.indexOf(newGenre) == -1)
            genres.add(newGenre);
    }

    public void removeGenre(Genre oldGenre) {
        if (genres.indexOf(oldGenre) != -1)
            genres.remove(genres.indexOf(oldGenre));
    }

    public void addRating(Rating newRating) {
        for (int i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).getUserName().equals(newRating.getUserName())) return;
        }
        ratings.add(newRating);
        setGrade();
    }

    public void removeRating(Rating oldRating) {
        int i;

        for (i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).getUserName().equals(oldRating.getUserName())) break;
        }

        if (i != ratings.size()) {
            ratings.remove(i);
        }
        setGrade();
    }

    public void removeRating(String userName) {
        int i;

        for (i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).getUserName().equals(userName)) break;
        }

        if (i != ratings.size()) {
            ratings.remove(i);
        }
        setGrade();
    }

    //methode to calculate the average grade of the production
    public double calculateGrade() {
        int sum = 0;

        for (int i = 0; i < this.ratings.size(); i++) {
            sum = sum + ratings.get(i).getGrade();
        }

        return (double) sum / (double) this.ratings.size();
    }

    public void setGrade() {
        this.grade = calculateGrade();
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int compareTo(Object o) {

        return this.title.compareTo(((LikableItems) o).giveName());

    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

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

}
