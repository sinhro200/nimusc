package nimusc.core.exceptions;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;

public enum LinkConvertingNE implements NimuscExceptionType {
    EMPTY_LINK("Link is empty.",0),
    BROKEN_LINK("Broken link",1),
    SONGS_NOT_FOUND("Songs not found.",2)
    ;

    private final static int CODE_PREFIX = 2;
    private final int code;
    private final String message;

    LinkConvertingNE(String message,int code) {
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

