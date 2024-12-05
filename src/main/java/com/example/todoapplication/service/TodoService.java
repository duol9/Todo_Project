package com.example.todoapplication.service;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;

public interface TodoService {
    TodoResponseDto saveTodo(TodoRequestDto dto);
}
