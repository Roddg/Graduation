package ru.javaops.graduation.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Vote deadline has already passed")  // 422
public class VoteDeadlineException extends RuntimeException {
    public VoteDeadlineException(String message) {
        super(message);
    }
}