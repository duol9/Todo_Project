package com.example.todoapplication.service;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import com.example.todoapplication.repository.TodoRespository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        // 2. DB에 저장 (Service -> repository, repository -> service)
        Todo savedTodo = todoRepository.saveTodo(todo);

        // 3.
        return new TodoResponseDto(savedTodo);
    }

    // 전체 일정 조회 ( 사용자의 요청에 대한 응답을 해줌(reponse) )
    @Override
    public ResponseEntity<List<TodoResponseDto>> findAllTodos(String name, LocalDate editDate) {
        // DB에 저장된 일정들을 전부 가지고옴 (repository -> service) ,dto형태로 반환..
        List<TodoResponseDto> dtoList = todoRepository.findAllTodos();
        List<TodoResponseDto> allTodos = new ArrayList<>();

        // 필터링 작업
        for (TodoResponseDto responseDto : dtoList) {

            if (responseDto.getName().equals(name) && responseDto.getEditDate().toLocalDate().equals(editDate) ) {
                allTodos.add(responseDto);
            } else if (name == null && responseDto.getEditDate().toLocalDate().equals(editDate)) {
                allTodos.add(responseDto);
            } else if (responseDto.getName().equals(name) && editDate == null) {
                allTodos.add(responseDto);
            } else if (name == null && editDate == null) {
                allTodos.add(responseDto);
            }
        }

        // 정렬 부분 추가 예정

        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    // 선택 일정 조회
    @Override
    public TodoResponseDto findTodoById(Long id) {
        Todo todo = todoRepository.findTodoById(id);

        // 해당 id의 일정이 없을 경우
        if(todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id);
        }

        return new TodoResponseDto(todo);
    }
}
