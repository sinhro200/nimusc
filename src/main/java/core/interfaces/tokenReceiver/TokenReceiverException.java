package core.interfaces.tokenReceiver;

import core.common.CommonException;

public class TokenReceiverException extends CommonException {
    TEType type;

    public TokenReceiverException(TEType type, String extra) {
        super(extra);
        this.type = type;
    }

    public TokenReceiverException(TEType type) {
        this(type,"");
    }

    @Override
    public String getMessage() {
        return type.text + extra;
    }

    @Override
    public String toString() {
        return "TokenException{" +
                "type=" + type +
                "("+type.text+")"+
                ", extra='" + extra + '\'' +
                '}';
    }

    enum TEType {
        REGISTRATION_ERROR("Registration error."),
        TOKEN_NOT_REFRESHED("Token was not refreshed, tokens are the same."),
        TOKEN_NOT_RECEIVED("Can't obtain token."),
        REQUEST_ERR("Error when making request."),
        TWOFA_REQ("Two factor auth is required."),
        TWOFA_ERR("2FA Error");

        String text;
        TEType(String text) {
            this.text = text;
        }
    }
}
