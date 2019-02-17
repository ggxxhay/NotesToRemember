package com.example.thanghq.notestoremember;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Note implements Serializable, Comparable<Note> {
    private int id;
    private String name;
    private String content;

    public Note() {
    }

    public Note(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Note(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Note otherNote) {
        return this.getName().toLowerCase().compareTo(otherNote.getName().toLowerCase());
    }
}
