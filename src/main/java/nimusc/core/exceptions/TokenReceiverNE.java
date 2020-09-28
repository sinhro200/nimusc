package nimusc.core.exceptions;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;

public enum TokenReceiverNE implements NimuscExceptionType {
    REGISTRATION_ERROR("Registration error.",0),
    TOKEN_NOT_REFRESHED("Token was not refreshed, tokens are the same.",1),
    TOKEN_NOT_RECEIVED("Can't obtain token.",2),
    TWOFA_REQ("Two factor auth is required.",4),
    TWOFA_ERR("2FA Error",5)
    ;

    private final static int CODE_PREFIX = 1;
    private final int code;
    private final String message;

    TokenReceiverNE(String message,int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return Integer.parseInt(String.valueOf(CODE_PREFIX) + code);
    }
}

