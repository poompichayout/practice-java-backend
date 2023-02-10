package com.poompich.training.backend.exception;

public class UserException extends BaseException {
    public UserException(String code) {
        super("user." + code);
    }

    public static UserException emailNull() {
        return new UserException("register.email.null");
    }

    public static UserException unauthorized() {
        return new UserException("unauthorized");
    }

    public static UserException requestNull() {
        return new UserException("register.request.null");
    }

    public static UserException createEmailNull() {
        return new UserException("create.email.null");
    }

    public static UserException createEmailDuplicated() {
        return new UserException("create.email.duplicated");
    }

    public static UserException createPasswordNull() {
        return new UserException("create.password.null");
    }

    public static UserException createNameNull() {
        return new UserException("create.email.null");
    }

    public static UserException loginFailedEmailNotFound() {
        return new UserException("login.failed");
    }

    public static UserException loginFailedPasswordNotMatched() {
        return new UserException("login.failed");
    }

    public static UserException notFound() {
        return new UserException("not.found");
    }


}
