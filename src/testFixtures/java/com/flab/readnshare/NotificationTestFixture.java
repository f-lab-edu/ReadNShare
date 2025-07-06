package com.flab.readnshare;

import com.flab.readnshare.domain.notification.domain.NotificationContent;

public class NotificationTestFixture {

    public static TestNotificationContent createTestNotificationContent(Long receiverId, String title, String body) {
        return new TestNotificationContent(
                receiverId,
                title,
                body
        );

    }

    public static class TestNotificationContent implements NotificationContent {
        private final Long receiverId;
        private final String title;
        private final String body;

        public TestNotificationContent(Long receiverId, String title, String body) {
            this.receiverId = receiverId;
            this.title = title;
            this.body = body;
        }

        @Override public Long getReceiverId() { return receiverId; }
        @Override public String getTitle() { return title; }
        @Override public String getBody() { return body; }
    }
}
