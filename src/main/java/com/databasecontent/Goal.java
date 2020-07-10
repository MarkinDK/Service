package com.databasecontent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Goal {
    private String title;
    private String description;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    private String userEmail;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Goal() {
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
