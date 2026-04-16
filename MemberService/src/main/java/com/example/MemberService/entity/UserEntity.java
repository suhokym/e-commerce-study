package com.example.MemberService.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String userName;


    public UserEntity() {

    }

    @Builder
    public UserEntity(String loginId, String userName) {
        this.loginId = loginId;
        this.userName = userName;
    }
}
