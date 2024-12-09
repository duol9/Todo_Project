package com.example.todoapplication.repository;

import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

@Repository
public class JdbcTemplateTodoRepository implements TodoRepository{

    private final JdbcTemplate jdbcTemplate;

    // 설정한 데이터소스 값대로 JdbcTemplate가 생성됨
    public JdbcTemplateTodoRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정 DB에 저장
    @Override
    public TodoResponseDto saveTodo(Todo todo) {
        // Insert 쿼리 작성하지 않아도 데이터 저장 됨
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        // todos 테이블에 id를 key값으로 설정해 Insert
        simpleJdbcInsert.withTableName("todos")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "pw", "task");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", todo.getName());
        parameters.put("pw", todo.getPw());
        parameters.put("task", todo.getTask());

        // db에 생성된 key값을 Number 타입으로 반환
        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new TodoResponseDto(key.longValue(), todo.getName(), todo.getPw(), todo.getTask(), null, null);
    }

    // 일정 전체 조회
    @Override
    public List<TodoResponseDto> findAllTodos(String name, LocalDate editDate) {

        List<TodoResponseDto> allTodos = new ArrayList<>(); // 해당하는 일정 담을 List
        List<Object> params = new ArrayList<>(); // WHERE절에 들어갈 parameter 리스트
        StringBuilder queryStringBuilder = new StringBuilder("SELECT * FROM todos"); // 공통 쿼리문

        // 작성자o, 수정일o -> WHERE절을 사용해 name과 editDate 모두 일치하는 행만 선택 (작성자, 수정일 조회)
        if (name != null && editDate != null){
            queryStringBuilder.append(" WHERE name = ? AND DATE(editDate) = ?");  // db의 editDate 타입은 DATETIME이라 날짜만 비교할 수 있게 DATE타입으로 형변환
            params.add(name);
            params.add(editDate);

            // 작성자x 수정일o 일 때 -> WHERE절을 시용헤 editDate와 일치하는 행만 선택 (작성자 조회)
        } else if (name == null && editDate != null) {
            queryStringBuilder.append(" WHERE DATE(editDate) = ?");
            params.add(editDate);

            // 작성자o, 수정일x -> WHERE절을 사용해 name과 일치하는 행만 선택 (수정일 조회)
        } else if (name != null && editDate == null) {
            queryStringBuilder.append(" WHERE name = ?");
            params.add(name);

            // 이름x, 수정일x -> 전체 조회이므로 WHERE절 없음
        } else if (name == null && editDate == null) {

        }

        queryStringBuilder.append(" ORDER BY editDate DESC");  // 수정일 기준 내림차순 정렬

        // jdbcTemplate.query(String sql쿼리, Object[] sql쿼리 ?에 들어갈 값들, RowMapper<T> ResultSet을 매핑해 원하는 객체로 변환);
        return jdbcTemplate.query(queryStringBuilder.toString(), params.toArray(), todoRowMapper());
    }

    // 일정 단건 조회
    @Override
    public Optional<TodoResponseDto> findTodoById(Long id) {
        List<TodoResponseDto> result = jdbcTemplate.query("SELECT * FROM todos WHERE id = ?", todoRowMapper(), id );
        // List를 단건 dto로 변환 후 반환
        return result.stream().findAny();
    }

    //해당하는 DB의 row를 가져와 객체로 변환
    private RowMapper<TodoResponseDto> todoRowMapper() {
        return (rs, rowNum) -> new TodoResponseDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("pw"),
                rs.getString("task"),
                rs.getTimestamp("createDate").toLocalDateTime(),
                rs.getTimestamp("editDate").toLocalDateTime()
        );
    }


    // 일정 수정 (작성자명, 할 일)
    @Override
    public int updateTodo(Long id, String name, String pw, String task) {
        StringBuilder queryStringBuilder = new StringBuilder("UPDATE todos SET ");
        List<Object> params = new ArrayList<>(); // ?에 들어갈 parameter 리스트

        // 작성자명, 할일 수정
        if (name != null && task != null) {
            queryStringBuilder.append("name = ?, task = ? ");
            params.add(name);
            params.add(task);

        // 작성자명만 수정
        } else if (name != null && task == null) {
            queryStringBuilder.append("name = ? ");
            params.add(name);

        // 할 읾만 수정
        } else if (name == null && task != null) {
            queryStringBuilder.append("task = ? ");
            params.add(task);
        }

        queryStringBuilder.append("WHERE id = ? AND pw = ?");
        params.add(id);
        params.add(pw);


        return jdbcTemplate.update(queryStringBuilder.toString(), params.toArray());
    }

    // 일정 삭제
    @Override
    public int deleteTodo(Long id, String pw) {
        return jdbcTemplate.update("DELETE FROM todos WHERE id = ? AND pw = ?", id, pw);
    }
}
