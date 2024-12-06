package com.example.todoapplication.repository;

import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoRespository {
    TodoResponseDto saveTodo(Todo todo);
    List<TodoResponseDto> findAllTodos(String name, LocalDate editDate);
    TodoResponseDto findTodoById(Long id);
}
