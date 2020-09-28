package nimusc.core.exceptions;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;

public enum LinkConvertingNE implements NimuscExceptionType {
    BROKEN_LINK("Broken link"),
    SONGS_NOT_FOUND("Songs not found.")
    ;
    
    private final String message;

    LinkConvertingNE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

