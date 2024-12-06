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
        //List<TodoResponseDto> allTodos = new ArrayList<>();

        // 스트림
        List<TodoResponseDto> filterTodos = dtoList.stream()
                .filter(todoResponseDto ->
                        matchesTodoFilter(todoResponseDto, name, editDate))
                .sorted((dto1, dto2) ->
                        dto2.getEditDate().compareTo(dto1.getEditDate()))
                .toList();

        /*
        // 필터링 작업
        for (TodoResponseDto responseDto : dtoList) {
            // 이름과 수정일이 모두 일치한 것들만 조회
            if (responseDto.getName().equals(name) && responseDto.getEditDate().toLocalDate().equals(editDate) ) {
                allTodos.add(responseDto);

                // 이름x 수정일이 일치한 것만 조회
            } else if (name == null && responseDto.getEditDate().toLocalDate().equals(editDate)) {
                allTodos.add(responseDto);

                // 수정일x, 이름이 일치한 것만 조회
            } else if (responseDto.getName().equals(name) && editDate == null) {
                allTodos.add(responseDto);

                // 이름x, 수정일x 전체 조회
            } else if (name == null && editDate == null) {
                allTodos.add(responseDto);
            }
        }

        /**
         * 수정일 기준 내림차순으로 정렬 (Stream X 버전)
         *  Comparator인터페이스의 compare를 오버라이드해 정렬 기준 정의.
         *  dto2의 수정일이 dto1의 수정일 이후면 양수 리턴
         *  수정일이 동일하면 0
         *  dto2의 수정일이 dto1의 수정일 이전이면 음수 리턴
         *
         */ /*
        Collections.sort(allTodos, new Comparator<TodoResponseDto>() {
            @Override
            public int compare(TodoResponseDto dto1, TodoResponseDto dto2) {
                return dto2.getEditDate().compareTo(dto1.getEditDate());
            }
        });
        */

        return new ResponseEntity<>(filterTodos, HttpStatus.OK);
    }

    private boolean matchesTodoFilter(TodoResponseDto dto, String name, LocalDate editDate) {

        // 작성자o, 수정일o -> dto에 들어있는 일정 작성자, 수정일과 일치하는지 검사 (작성자, 수정일 조회)
        if (name != null && editDate != null){
            return dto.getName().equals(name) && dto.getEditDate().toLocalDate().equals(editDate);

            // 작성자x 수정일o 일 때 -> dto에 들어있는 일정 수정일과 일치하는지 검사 (작성자 조회)
        } else if (name == null && editDate != null) {
            return dto.getEditDate().toLocalDate().equals(editDate);

            // 작성자o, 수정일x -> dto에 들어있는 일정 작성자와 일치하는지 검사 (수정일 조회)
        } else if (name != null && editDate == null) {
            return dto.getName().equals(name);

            // 이름x, 수정일x -> 전부 true (전체 조회)
        } else if (name == null && editDate == null) {
            return true;
        }
        // 위 조건에 해당하지 않은 dto는 false 반환
        return false;
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
