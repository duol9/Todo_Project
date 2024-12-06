package com.example.todoapplication.controller;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// controller -> Service
@RestController
@RequestMapping("/todos")
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

    // 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> findAllTodos(@RequestParam(required = false) String name, @RequestParam(required = false) LocalDate editDate){
        return todoService.findAllTodos(name, editDate);
    }

    // id로 선택 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> findTodoById(@PathVariable Long id) {
        return new ResponseEntity<>(todoService.findTodoById(id), HttpStatus.OK);
    }

    // 일정 수정
    @PatchMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto dto) {
        return new ResponseEntity<>(todoService.updateTodo(id, dto.getName(), dto.getPw(), dto.getTask()), HttpStatus.OK);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<TodoResponseDto> deleteTodo(@PathVariable Long id, @RequestBody TodoRequestDto dto) {
        todoService.deleteTodo(id, dto.getPw());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
