package com.example.todoapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Todo {

    private Long id;
    private String name;
    private String pw;
    private String task;
    private LocalDateTime createDate;
    private LocalDateTime editDate;

    public Todo(String name, String pw, String task) {
        this.name = name;
        this.pw = pw;
        this.task = task;
    }
}
