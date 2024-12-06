package com.example.todoapplication.repository;

import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateTodoRepository implements TodoRespository{

    private final JdbcTemplate jdbcTemplate;

    // 설정한 데이터소스 값대로 JdbcTemplate가 생성됨
    public JdbcTemplateTodoRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public TodoResponseDto saveTodo(Todo todo) {
        // Insert 쿼리 작성하지 않아도 데이터 저장 됨
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        // todos 테이블에 id를 key값으로 설정해 Insert
        simpleJdbcInsert.withTableName("todos").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", todo.getName());
        parameters.put("pw", todo.getPw());
        parameters.put("task", todo.getTask());
        //parameters.put("createDate", todo.getCreateDate());
        //parameters.put("editDate", todo.getEditDate());

        // db에 생성된 key값을 Number 타입으로 반환
        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new TodoResponseDto(key.longValue(), todo.getName(), todo.getPw(), todo.getTask(),null , null);
    }

    @Override
    public Todo findTodoById(Long id) {
        return null;
    }

    @Override
    public List<TodoResponseDto> findAllTodos() {

        return List.of();
    }
}
