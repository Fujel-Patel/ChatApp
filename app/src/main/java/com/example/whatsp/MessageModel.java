package com.example.whatsp;

import java.util.Objects;

public class MessageModel {
    private String messageId;
    private String message;
    private String senderId;
    private long timestamp; // Added timestamp field

    // No-argument constructor
    public MessageModel() {
    }

    // Parameterized constructor
    public MessageModel(String messageId, String message, String senderId, long timestamp) {
        this.messageId = Objects.requireNonNull(messageId, "Message ID cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.senderId = Objects.requireNonNull(senderId, "Sender ID cannot be null");
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setMessageId(String messageId) {
        this.messageId = Objects.requireNonNull(messageId, "Message ID cannot be null");
    }

    public void setMessage(String message) {
        this.message = Objects.requireNonNull(message, "Message cannot be null");
    }

    public void setSenderId(String senderId) {
        this.senderId = Objects.requireNonNull(senderId, "Sender ID cannot be null");
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Optional: Override toString, equals, and hashCode
    @Override
    public String toString() {
        return "MessageModel{" +
                "messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageModel that = (MessageModel) o;
        return timestamp == that.timestamp &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(message, that.message) &&
                Objects.equals(senderId, that.senderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, message, senderId, timestamp);
    }
}
