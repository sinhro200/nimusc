package core.interfaces.linkToSongConverter;

import core.common.CommonException;

public class LinkConvertingException extends CommonException {

    public LinkConvertingException(LCEType type, String extra) {
        super(type.message,extra);
    }

    public LinkConvertingException(LCEType type) {
        this(type,null);
    }

    public enum LCEType {
        ACCESS_TOKEN_BROKEN("Access token is broken."),
        ACCESS_TOKEN_EMPTYORNULL("Access token is empty or null."),
        EMPTY_LINK("Link is empty."),
        BROKEN_LINK("Broken link"),
        REQUEST_ERR("Error when making request."),
        SONGS_NOT_FOUND("Songs not found."),
        WRONG_INPUT_DATA("Wrong input data"),
        ;
        String message;

        LCEType(String message) {
            this.message = message;
        }
    }
}
