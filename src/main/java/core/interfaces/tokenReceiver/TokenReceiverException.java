package core.interfaces.tokenReceiver;

import core.common.CommonException;

public class TokenReceiverException extends CommonException {

    public TokenReceiverException(TEType type, String extra) {
        super(type.text,extra);
    }

    public enum TEType {
        REGISTRATION_ERROR("Registration error."),
        TOKEN_NOT_REFRESHED("Token was not refreshed, tokens are the same."),
        TOKEN_NOT_RECEIVED("Can't obtain token."),
        REQUEST_ERR("Error when making request."),
        TWOFA_REQ("Two factor auth is required."),
        TWOFA_ERR("2FA Error"),
        WRONG_INPUT_DATA("Wrong input data"),
        ;

        String text;
        TEType(String text) {
            this.text = text;
        }
    }
}
