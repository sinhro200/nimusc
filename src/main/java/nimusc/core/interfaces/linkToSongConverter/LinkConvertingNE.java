package nimusc.core.interfaces.linkToSongConverter;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;

public enum LinkConvertingNE implements NimuscExceptionType {
    ACCESS_TOKEN_BROKEN("Access token is broken."),
    ACCESS_TOKEN_EMPTYORNULL("Access token is empty or null."),
    EMPTY_LINK("Link is empty."),
    BROKEN_LINK("Broken link"),
    REQUEST_ERR("Error when making request."),
    SONGS_NOT_FOUND("Songs not found."),
    WRONG_INPUT_DATA("Wrong input data"),
    ;
    String message;

    LinkConvertingNE(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

