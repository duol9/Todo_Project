package com.example.todoapplication.dto;

import com.example.todoapplication.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoResponseDto {

    private Long id;
    private String name;
    private String pw;
    private String task;
    private LocalDateTime createDate;
    private LocalDateTime editDate;

    public TodoResponseDto(Todo todo){
        this.id = todo.getId();
        this.name = todo.getName();
        this.pw = todo.getPw();
        this.task = todo.getTask();
        this.createDate = todo.getCreateDate();
        this.editDate = todo.getEditDate();
    }
}
