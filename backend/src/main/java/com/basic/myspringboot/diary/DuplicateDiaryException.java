package com.basic.myspringboot.diary.exception;

public class DuplicateDiaryException extends RuntimeException {
    public DuplicateDiaryException(String message) {
        super(message);
    }
}
