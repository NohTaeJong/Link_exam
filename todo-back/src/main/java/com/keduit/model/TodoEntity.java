package com.keduit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "Todo")
public class TodoEntity {
    @Id
    // 나만의 Generator를 사용하고 싶을 때 사용
    // uuid를 사용하는 system-uuid라는 이름의 GenericGenerator를 만들었고
    // Generator에서 참조
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String userId;
    private String title;
    private boolean done;
}
