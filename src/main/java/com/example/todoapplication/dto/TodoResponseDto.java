package com.example.todoapplication.dto;

import com.example.todoapplication.entity.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
// 응답 시 비번 제외하고 반환
public class TodoResponseDto {

    private Long id;
    private String name;
    @JsonIgnore
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
