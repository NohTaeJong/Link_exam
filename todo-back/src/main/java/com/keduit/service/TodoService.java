package com.keduit.service;

import com.keduit.model.TodoEntity;
import com.keduit.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;

    public String testService() {

        TodoEntity todo = TodoEntity.builder()
                        .title("My first todo list")
                .build();

        todoRepository.save(todo);
        TodoEntity saveEntity = todoRepository.findById(todo.getId()).get();

        return saveEntity.getTitle();
    }

    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity is null");
            throw new RuntimeException("Entity is null");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> create(TodoEntity todoEntity) {
        validate(todoEntity);
        todoRepository.save(todoEntity);
        log.info("EntityID : {} is saved.", todoEntity.getId());
        return todoRepository.findByUserId(todoEntity.getUserId());
    }

    public List<TodoEntity> read(String userId) {
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
        original.ifPresent(
                todo -> {
                    todo.setTitle(entity.getTitle());
                    todo.setDone(entity.isDone());
                    todoRepository.save(todo);
                }
        );

        return read(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity todoEntity) {
        validate(todoEntity);

        try{
            todoRepository.delete(todoEntity);
        } catch (Exception e) {
            log.error("delete error", todoEntity.getId(), e);
            throw new RuntimeException("delete error" + todoEntity.getId());
        }

        return todoRepository.findByUserId(todoEntity.getUserId());
    }
}
