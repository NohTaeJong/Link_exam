package com.keduit.controller;

import com.keduit.dto.ResponseDto;
import com.keduit.dto.TodoDto;
import com.keduit.model.TodoEntity;
import com.keduit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController // 알아서 JSON으로 변환
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping(value = "/test")
    public ResponseEntity<?> testTodo() {
        String str = todoService.testService();
        List<String> list = new ArrayList<>();

        list.add(str);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .data(list).build();

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping
    public ResponseEntity selectTodo(@AuthenticationPrincipal String userId) {
        try{
            List<TodoEntity> read = todoService.read(userId);

            List<TodoDto> collect = read.stream().map(TodoDto::new).collect(Collectors.toList());

            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder()
                    .data(collect)
                    .build();

            return ResponseEntity.ok().body(build);

        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto build = ResponseDto.builder()
                    .error(error)
                    .build();

            return ResponseEntity.badRequest().body(build);
        }
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto todoDto,
                                        @AuthenticationPrincipal String userId) {
        try {

            TodoEntity entity = TodoDto.toEntity(todoDto);
            entity.setId(null);
            entity.setUserId(userId);
            
            // 서비스로부터 엔티티 List를 가져옴
            List<TodoEntity> entities = todoService.create(entity);
            
            // 엔티티 List를 dto리스트로 변환
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            
            // 변환된 dto List를 ResponseDto로 값 설정
            ResponseDto<TodoDto> build = ResponseDto.<TodoDto>builder().data(dtos).build();

            // ResponseDto Return
            return ResponseEntity.ok().body(build);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto todoDto,
                                        @AuthenticationPrincipal String userId) {

        TodoEntity entity = TodoDto.toEntity(todoDto);
        entity.setUserId(userId);

        List<TodoEntity> entities = todoService.update(entity);
        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                .data(dtos)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto todoDto,
                                        @AuthenticationPrincipal String userId) {
        try{

            TodoEntity todoEntity = TodoDto.toEntity(todoDto);
            todoEntity.setUserId(userId);

            List<TodoEntity> todoEntities = todoService.delete(todoEntity);
            List<TodoDto> dtos = todoEntities.stream().map(TodoDto::new).collect(Collectors.toList());
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

}
