package com.flab.readnshare.domain.member.domain;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String nickName;

    @OneToMany(mappedBy = "fromMember")
    private List<Follow> followings;

    @OneToMany(mappedBy = "toMember")
    private List<Follow> followers;

    @Builder
    public Member(Long id, String email, String password, String nickName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
