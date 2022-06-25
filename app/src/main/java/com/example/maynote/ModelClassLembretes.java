package com.example.maynote;

public class ModelClassLembretes {
    private String title,date,hour, dataPost;

    public ModelClassLembretes(String title, String date, String hour, String dataPost) {
        this.title = title;
        this.date = date;
        this.hour = hour;
        this.dataPost = dataPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDataPost() {
        return dataPost;
    }

    public void setDataPost(String dataPost) {
        this.dataPost = dataPost;
    }
}
