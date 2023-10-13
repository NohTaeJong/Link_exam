package com.keduit.repository;

import com.keduit.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    List<TodoEntity> findByUserId(String userId);

    @Query("select t from TodoEntity t where t.userId = ?1")
    TodoEntity findByUserIdQuery(String userId);

}
