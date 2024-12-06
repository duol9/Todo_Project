package com.example.todoapplication.repository;

import com.example.todoapplication.dto.TodoResponseDto;
import com.example.todoapplication.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    //
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

    @Override
    public Todo findTodoById(Long id) {
        // List<T> 형태로 반환해주는 query와 달리 단일 반환
        return jdbcTemplate.queryForObject("SELECT * FROM todos WHERE id = ?", todoRowMapper2(), id );
    }

    private RowMapper<Todo> todoRowMapper2() {
        return new RowMapper<Todo>() {
            @Override
            public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Todo(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("pw"),
                        rs.getString("task"),
                        rs.getTimestamp("createDate").toLocalDateTime(),
                        rs.getTimestamp("editDate").toLocalDateTime()
                );
            }
        };
    }
}
