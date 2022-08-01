package com.seventeam.algoritmgameproject.domain.model.login;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USER_INFO")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;//login

    @Column(nullable = false)
    private String avatarUrl;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @ColumnDefault("0")
    private int winCnt;

    @Column
    @ColumnDefault("0")
    private int loseCnt;
    @Column
    private String role;

    @Builder
    private User(String userId, String avatarUrl, String email, String password) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.password = password;
        this.role = "ROLE_USER";
    }

    public void win(){
        this.winCnt++;
    }

    public void lose(){
        this.loseCnt++;
    }

}