package com.flab.readnshare.domain.follow.domain;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"from_member", "to_member"})
})
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_member")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member")
    private Member toMember;

    @Builder
    public Follow(Long id, Member fromMember, Member toMember) {
        this.id = id;
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
