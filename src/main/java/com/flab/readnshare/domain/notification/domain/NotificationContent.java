package com.flab.readnshare.domain.notification.domain;

public interface NotificationContent {
    String getTitle();

    String getBody();

    Long getReceiverId();
}
