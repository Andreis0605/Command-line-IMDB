package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Series extends Production implements LikableItems {
    private int releaseYear;

    private int numOfSeasons;

    private HashMap<String, ArrayList<Episode>> seasons;

    @Override
    public void displayInfo() {
        System.out.println(this);
    }

    public Series() {
        this.releaseYear = 0;

        this.numOfSeasons = 0;

        this.seasons = new HashMap<String, ArrayList<Episode>>();
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getNumOfSeasons() {
        return numOfSeasons;
    }

    public HashMap<String, ArrayList<Episode>> getSeasons() {
        return seasons;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setNumOfSeasons(int numOfSeasons) {
        this.numOfSeasons = numOfSeasons;
    }

    public void addSeason(String name) {
        seasons.put(name, new ArrayList<Episode>());
    }

    public void addEpisodeInSeason(String name, Episode episode) {
        seasons.get(name).add(episode);
    }

    public void addSeason(String key, ArrayList<Episode> episodesList)
    {
        seasons.put(key,episodesList);
    }

    @Override
    public String giveName() {
        return getTitle();
    }

    public String toString() {
        String aux = "";
        if (this.getTitle() != null) aux += "Title: " + this.getTitle() + "\n";
        if (this.getDirectors() != null) {
            aux += "Directors: ";
            for (int i = 0; i < getDirectors().size() - 1; i++) {
                aux += getDirectors().get(i) + ", ";
            }
            if (getDirectors().size() > 0) aux += getDirectors().get(getDirectors().size() - 1) + "\n";
        }
        if (this.getActors() != null) {
            aux += "Actors: ";
            for (int i = 0; i < getActors().size() - 1; i++) {
                aux += getActors().get(i) + ", ";
            }
            if (getActors().size() > 0) aux += getActors().get(getActors().size() - 1) + "\n";
        }
        if (this.getGenres() != null) {
            aux += "Genres: ";
            for (int i = 0; i < getGenres().size() - 1; i++) {
                aux += getGenres().get(i).toString() + ", ";
            }
            if (getGenres().size() > 0) aux += getGenres().get(getGenres().size() - 1) + "\n";
        }
        if (this.getDescription() != null) {
            aux += "Plot: " + this.getDescription() + "\n";
        }
        if (this.getGrade() != -1) {
            aux += "Grade: " + this.getGrade() + "\n";
        }
        if (this.getReleaseYear() != 0) {
            aux += "Release year: " + this.getReleaseYear() + "\n";
        }
        if (this.getNumOfSeasons() != 0) {
            aux += "Number of seasons: " + this.getNumOfSeasons() + "\n";
        }
        if (this.getSeasons() != null) {
            for (int counter = 1 ; counter<= this.numOfSeasons ; counter++) {
                aux += "Season " + counter + ":" + "\n\n";
                for (int i = 0; i < seasons.get("Season " + counter).size(); i++) {
                    aux += "Episode: " + seasons.get("Season " + counter).get(i).getName() + "   Duration: " + seasons.get("Season " + counter).get(i).getDuration() + " minutes\n";
                }
                aux += "\n\n";
            }
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


}
