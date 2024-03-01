package org.example;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Movie extends Production implements LikableItems {
    private int duration;

    private int releaseYear;

    @Override
    public void displayInfo() {
        System.out.println(this);
    }

    public String toString() {
        String aux = "";
        if (this.getTitle() != null) aux += "Title: " + this.getTitle() + "\n";
        if (this.getDirectors() != null) {
            aux += "Directors: ";
            for (int i = 0; i < getDirectors().size() - 1; i++) {
                aux += getDirectors().get(i) + ", ";
            }
            if(getDirectors().size()>0) aux += getDirectors().get(getDirectors().size() - 1) + "\n";
        }
        if (this.getActors() != null) {
            aux += "Actors: ";
            for (int i = 0; i < getActors().size() - 1; i++) {
                aux += getActors().get(i) + ", ";
            }
            if(getActors().size()>0) aux += getActors().get(getActors().size() - 1) + "\n";
        }
        if (this.getGenres() != null) {
            aux += "Genres: ";
            for (int i = 0; i < getGenres().size() - 1; i++) {
                aux += getGenres().get(i).toString() + ", ";
            }
            if(getGenres().size()>0)  aux += getGenres().get(getGenres().size() - 1) + "\n";
        }
        if (this.getDescription() != null) {
            aux += "Plot: " + this.getDescription() + "\n";
        }
        if (this.getGrade() != -1) {
            aux += "Grade: " + this.getGrade() + "\n";
        }
        if (this.getDuration() != -1) {
            aux += "Duration: " + this.getDuration() + "\n";
        }

        if (this.getReleaseYear() != -1) {
            aux += "Release year: " + this.getReleaseYear() + "\n";
        }
        if (this.getRatings() != null) {

            Collections.sort(this.getRatings(), (o1, o2) -> {
                User auxUser1 = null,auxUser2 = null;
                for(User auxUser : IMDB.getInstance().getUsersList())
                {
                    if(auxUser.getUsername().equals(o1.getUserName())) auxUser1 = auxUser;
                }
                for(User auxUser : IMDB.getInstance().getUsersList())
                {
                    if(auxUser.getUsername().equals(o2.getUserName())) auxUser2 = auxUser;
                }
                return auxUser2.getExperience()-auxUser1.getExperience();
            });

            aux += "Ratings: " + "\n\n";
            for (int i = 0; i < this.getRatings().size(); i++) {
                aux += "User: " + this.getRatings().get(i).getUserName() + "\n";
                aux += "Grade: " + this.getRatings().get(i).getGrade() + "\n";
                if (this.getRatings().get(i).getComments() != null) {
                    aux += "Comment: " + this.getRatings().get(i).getComments();
                }
                aux += "\n\n";
            }
        }
        return aux;
    }

    public Movie() {
        super();
        this.duration = -1;
        this.releaseYear = -1;
    }

    public Movie(String title, ArrayList<String> directors, ArrayList<String> actors,
                 ArrayList<Genre> genres, String description, int duration, int releaseYear) {
        super(title, directors, actors, genres, description);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String giveName() {
        return getTitle();
    }
}
