package com.example.nickomarsellino.scheduling;

/**
 * Created by nicko marsellino on 3/18/2018.
 */

public class Schedule {

    private long id;
    private String title;
    private String content;
    private String date;


    public Schedule(){

    }

    public Schedule(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

}
