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

    public static UserException loginFailedUserUnactivated() {
        return new UserException("login.failed.unactivated");
    }

    public static UserException notFound() {
        return new UserException("not.found");
    }

    // Activate
    public static UserException activateNoToken() {
        return new UserException("activate.token.not.found");
    }

    public static UserException activateFail() {
        return new UserException("activate.failed");
    }

    public static UserException activateTokenExpired() {
        return new UserException("activate.token.expired");
    }

    public static UserException activateFailAlreadyActivated() {
        return new UserException("already.activated");
    }

    // Resend Activation Email
    public static UserException resendActivationEmailNoEmail() {
        return new UserException("resend.activation.no.email");
    }

    public static UserException resendActivationEmailUserNotFound() {
        return new UserException("resend.activation.user.not.found");
    }
}
