package com.example.domain;

public class Author {

    public final long id;
    public final String name;
    public final String username;
    public final String password;

    public Author(long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

}
