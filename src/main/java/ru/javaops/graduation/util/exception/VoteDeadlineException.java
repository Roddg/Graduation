package ru.javaops.graduation.util.exception;

public class VoteDeadlineException extends RuntimeException {
    public VoteDeadlineException(String message) {
        super(message);
    }
}