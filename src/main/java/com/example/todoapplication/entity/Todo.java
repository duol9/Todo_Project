package com.example.todoapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Todo {

    private Long id;
    private String name;
    private String pw;
    private String task;
    @Setter
    private LocalDateTime createDate;
    @Setter
    private LocalDateTime editDate;

    public Todo(String name, String pw, String task) {
        this.name = name;
        this.pw = pw;
        this.task = task;
        //this.createDate = LocalDateTime.now();
        //this.editDate = this.createDate;
    }

}
