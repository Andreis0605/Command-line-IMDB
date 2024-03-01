package org.example;

public class Episode {
    private String name;

    private int duration;

    public Episode()
    {
        this.name = "";
        this.duration = 0;
    }

    public Episode(String name, int duration)
    {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
