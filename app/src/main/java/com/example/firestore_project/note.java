package com.example.firestore_project;

public class note {
    String title;
    String description;

    public note() {
    }
    public note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
