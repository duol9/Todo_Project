package com.example.todoapplication.service;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
    TodoResponseDto saveTodo(TodoRequestDto dto);
    ResponseEntity<List<TodoResponseDto>> findAllTodos(String name, LocalDate editDate);
    TodoResponseDto findTodoById(Long id);
    TodoResponseDto updateTodo(Long id, String name, String pw, String task);
}
