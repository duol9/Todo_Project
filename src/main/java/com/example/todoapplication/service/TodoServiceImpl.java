package com.example.todoapplication.service;

import com.example.todoapplication.dto.TodoRequestDto;
import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import com.example.todoapplication.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 일정 저장
    @Override
    public TodoResponseDto saveTodo(TodoRequestDto dto) {
        // 1. 사용자로부터 입력 받은 일정 저장
        Todo todo = new Todo(dto.getName(), dto.getPw(), dto.getTask());

        // 2. DB에 저장 (Service -> repository)
        return todoRepository.saveTodo(todo);
    }

    // 전체 일정 조회 ( 사용자의 요청에 대한 응답을 해줌(reponse) )
    @Override
    public ResponseEntity<List<TodoResponseDto>> findAllTodos(String name, LocalDate editDate) {
        // DB에 저장된 일정들을 전부 가지고옴 (repository -> service) ,dto형태로 반환..
        List<TodoResponseDto> allTodos = todoRepository.findAllTodos(name, editDate);
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    // 선택 일정 조회
    @Override
    public TodoResponseDto findTodoById(Long id) {
        Optional<TodoResponseDto> todoResponseDto = todoRepository.findTodoById(id);

        // 해당 id의 일정이 없을 경우
        if(todoResponseDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id);
        }

        return todoResponseDto.get();
    }

    // 일정 수정
    @Transactional
    @Override
    public TodoResponseDto updateTodo(Long id, String name, String pw, String task) {

        // 필수값 검증
        if (name == null && task == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name or task are required values.");
        }

        // 수정된 row 개수
        int updateRow = todoRepository.updateTodo(id, name, pw, task);

        // 수정된 일정이 0개라면
        if(updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }

        return todoRepository.findTodoById(id).get();
    }

    // 일정 삭제
    @Override
    public void deleteTodo(Long id, String pw) {
        // repository를 통해 삭제된 row(일정)의 수
        int deleteRow = todoRepository.deleteTodo(id, pw);
        // 삭제된 row(일정)가 0개 라면
        if (deleteRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}
