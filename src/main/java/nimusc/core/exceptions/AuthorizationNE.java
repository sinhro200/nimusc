package nimusc.core.exceptions;

import nimusc.core.common.exception.NimuscExceptionType;

public enum AuthorizationNE implements NimuscExceptionType {
    REGISTRATION_ERROR("Registration error."),
    TOKEN_NOT_REFRESHED("Token was not refreshed, tokens are the same."),
    TOKEN_NOT_RECEIVED("Can't obtain token."),
    TWOFA_REQ("Two factor auth is required."),
    TWOFA_ERR("2FA Error"),
    UNAUTHORIZED("Unauthorized"),
    ;
    
    private final String message;

    AuthorizationNE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

