package com.databasecontent.Task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Calendar;

@JsonAutoDetect
public class Task {
    private String title;
    private String description;

    public void setCompleteBy(Calendar completeBy) {
        this.completeBy = completeBy;
    }

    @JsonSerialize(using = MyCalendarSerializer.class)
    @JsonDeserialize(using = MyCalendarDeserializer.class)
    private Calendar completeBy;
    private boolean completed;
    private Integer id;
    private Integer goalId=null;
    private String userEmail;

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Task() {
    }

    public Calendar getCompleteBy() {
        return completeBy;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setTitle(String title) {
        if (title != null)
            this.title = title;
        else this.title = "default";
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
