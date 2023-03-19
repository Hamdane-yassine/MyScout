package com.hamdane.myscoutmediasvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class VideoException extends RuntimeException {
    public VideoException(String message) {
        super(message);
    }

    public VideoException(String message, Exception exception) {
        super(message, exception);
    }
}
