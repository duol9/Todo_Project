package com.example.todoapplication.repository;

import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    TodoResponseDto saveTodo(Todo todo);
    List<TodoResponseDto> findAllTodos(String name, LocalDate editDate);
    Optional<TodoResponseDto> findTodoById(Long id);
    int updateTodo(Long id, String name, String pw, String task);
    int deleteTodo(Long id, String pw);
}
