package nimusc.core.exceptions;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;


public enum SongFinderNE implements NimuscExceptionType {
    SONG_NOT_FOUND("Songs not found."),
    ;
    
    private final String message;



    SongFinderNE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

