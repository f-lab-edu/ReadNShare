package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.notification.domain.NotificationContent;

public interface NotificationSender<T extends NotificationContent> {
    void sendNotification(T content);
}
