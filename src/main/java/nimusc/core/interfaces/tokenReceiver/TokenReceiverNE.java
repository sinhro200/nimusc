package nimusc.core.interfaces.tokenReceiver;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;

public enum TokenReceiverNE implements NimuscExceptionType {
    REGISTRATION_ERROR("Registration error."),
    TOKEN_NOT_REFRESHED("Token was not refreshed, tokens are the same."),
    TOKEN_NOT_RECEIVED("Can't obtain token."),
    REQUEST_ERR("Error when making request."),
    TWOFA_REQ("Two factor auth is required."),
    TWOFA_ERR("2FA Error"),
    WRONG_INPUT_DATA("Wrong input data"),
    ;

    String text;
    TokenReceiverNE(String text) {
        this.text = text;
    }

    @Override
    public String getMessage() {
        return text;
    }
}

