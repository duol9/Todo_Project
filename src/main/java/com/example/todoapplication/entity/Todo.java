package com.example.todoapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Todo {

    @Setter
    private Long id;
    private String name;
    private String pw;
    private String title;
    private String task;
    private String date;

    public Todo(String name, String pw, String title, String task) {
        this.name = name;
        this.pw = pw;
        this.title = title;
        this.task = task;
    }

}
