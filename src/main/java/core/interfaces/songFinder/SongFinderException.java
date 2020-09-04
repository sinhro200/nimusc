package core.interfaces.songFinder;

import core.common.CommonException;

public class SongFinderException extends CommonException {

    public SongFinderException(SFEType songNotFound, String extra) {
        super(songNotFound.message,extra);
    }
    public enum SFEType{
        ACCESS_TOKEN_BROKEN("Access token is broken."),
        ACCESS_TOKEN_EMPTYORNULL("Access token is empty or null."),
        REQUEST_ERR("Error when making request."),
        SONG_NOT_FOUND("Songs not found."),
        WRONG_INPUT_DATA("Wrong input data"),
        ;
        String message;

        SFEType(String message) {
            this.message = message;
        }
    }
}
