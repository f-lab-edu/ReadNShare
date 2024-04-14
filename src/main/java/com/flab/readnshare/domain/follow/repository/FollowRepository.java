package com.flab.readnshare.domain.follow.repository;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
