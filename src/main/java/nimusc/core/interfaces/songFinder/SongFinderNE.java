package nimusc.core.interfaces.songFinder;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;


public enum SongFinderNE implements NimuscExceptionType {
    ACCESS_TOKEN_BROKEN("Access token is broken."),
    ACCESS_TOKEN_EMPTYORNULL("Access token is empty or null."),
    REQUEST_ERR("Error when making request."),
    SONG_NOT_FOUND("Songs not found."),
    WRONG_INPUT_DATA("Wrong input data"),
    ;
    String message;

    SongFinderNE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

