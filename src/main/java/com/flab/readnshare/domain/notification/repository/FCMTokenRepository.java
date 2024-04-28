package com.flab.readnshare.domain.notification.repository;

import com.flab.readnshare.domain.notification.domain.FCMToken;
import org.springframework.data.repository.CrudRepository;

public interface FCMTokenRepository extends CrudRepository<FCMToken, Long> {
}
