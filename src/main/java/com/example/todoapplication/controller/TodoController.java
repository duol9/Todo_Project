package com.example.todoapplication.controller;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    // 생성자 주입
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto requestDto) {
        return new ResponseEntity<>(todoService.saveTodo(requestDto), HttpStatus.OK);
    }
}
