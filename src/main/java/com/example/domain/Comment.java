package com.example.domain;

import java.time.LocalDateTime;

public class Comment {

    public final long id;
    public final long postId;
    public final String name;
    public final String text;
    public final LocalDateTime entryDate;
/*
    public Comment(String name, String text, LocalDateTime entryDate, long postId) {
        this.name = name;
        this.text = text;
        this.entryDate = entryDate;
        this.postId = postId;
    }
    */
    public Comment(long id, String name, String text, LocalDateTime entryDate, long postId) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.entryDate = entryDate;
        this.postId = postId;
    }
}
