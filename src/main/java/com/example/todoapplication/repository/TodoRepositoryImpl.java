package com.example.todoapplication.repository;

import com.example.todoapplication.entity.Todo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TodoRepositoryImpl implements TodoRespository {

    // 시스템 id와 사용자가 입력한 일정을 map에 저장
    private final Map<Long, Todo> todoList = new HashMap<>();

    // DB에 일정 저장
    @Override
    public Todo saveTodo(Todo todo) {

        // 시스템 id 생성 후 저장
        Long todoId = todoList.isEmpty() ? 1 : Collections.max(todoList.keySet()) + 1;
        todo.setId(todoId);

        // 작성일 생성 후 저장 (수정일도)
        todo.setCreateDate(LocalDateTime.now());
        todo.setEditDate(todo.getCreateDate());

        // 생성한 아이디와 알정을 db에 저장
        todoList.put(todoId, todo);

        return todo;
    }
}
