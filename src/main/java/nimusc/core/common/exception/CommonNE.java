package nimusc.core.common.exception;

public enum CommonNE implements NimuscExceptionType{
    WRONG_HTTP_URL_PARAMETERS,
    ERR_WHILE_SENDING_REQUEST,
    ERR_IN_RESPONSE,
    ACCESS_TOKEN_BROKEN,
    ACCESS_TOKEN_EMPTYORNULL,
    ;
    @Override
    public String getMessage() {
        return this.name()+".";
    }

}
