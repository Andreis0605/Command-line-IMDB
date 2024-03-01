package org.example;

public class Rating {
    private String userName;

    private int grade;

    private String comments;

    public Rating(String UserName, int Grade, String Comments) {
        this.userName = UserName;
        this.grade = Grade;
        this.comments = Comments;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getGrade() {
        return grade;
    }

    public String getComments() {
        return comments;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return this.userName + " " + this.grade + " " + this.comments;
    }
}
