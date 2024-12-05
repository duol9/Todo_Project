package com.example.todoapplication.service;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import com.example.todoapplication.repository.TodoRespository;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRespository todoRepository;

    public TodoServiceImpl(TodoRespository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 일정 저장
    @Override
    public TodoResponseDto saveTodo(TodoRequestDto dto) {
        // 1. 사용자로부터 입력 받은 일정 저장
        Todo todo = new Todo(dto.getName(), dto.getPw(), dto.getTitle(), dto.getTask());

        // 2. DB에 저장
        Todo savedTodo = todoRepository.saveTodo(todo);
        return new TodoResponseDto(savedTodo);
    }
}
