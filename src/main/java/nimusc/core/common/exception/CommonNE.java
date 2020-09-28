package nimusc.core.common.exception;

public enum CommonNE implements NimuscExceptionType{
    WRONG_HTTP_URL_PARAMETERS(1),
    ERR_WHILE_SENDING_REQUEST(0),
    ERR_IN_RESPONSE(2),
    ACCESS_TOKEN_BROKEN(3),
    ACCESS_TOKEN_EMPTYORNULL(4),
    ;

    private final static int CODE_PREFIX = 0;
    private int code;

    CommonNE(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.name()+".";
    }

    @Override
    public int getCode() {
        return Integer.parseInt(String.valueOf(CODE_PREFIX) + code);
    }
}
