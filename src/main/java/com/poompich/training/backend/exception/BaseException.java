package com.poompich.training.backend.exception;

public abstract class BaseException extends Exception {
    public BaseException(String code) {
        super(code);
    }
}
