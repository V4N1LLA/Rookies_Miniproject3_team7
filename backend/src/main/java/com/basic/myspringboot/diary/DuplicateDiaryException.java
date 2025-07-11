package com.basic.myspringboot.diary;

public class DuplicateDiaryException extends RuntimeException {
    public DuplicateDiaryException(String message) {
        super(message);
    }
}
