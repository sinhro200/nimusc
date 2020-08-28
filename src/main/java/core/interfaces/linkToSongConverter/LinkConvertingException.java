package core.interfaces.linkToSongConverter;

import core.common.CommonException;

public class LinkConvertingException extends CommonException {
    private LCEType type;

    public LinkConvertingException(LCEType type, String extra) {
        super(extra);
        this.type = type;
    }

    public LinkConvertingException(LCEType type) {
        this(type,null);
    }

    @Override
    public String getMessage() {
        return type.message + this.extra;
    }

    public enum LCEType {
        ACCESS_TOKEN_BROKEN("Access token is broken."),
        ACCESS_TOKEN_EMPTYORNULL("Access token is empty or null."),
        EMPTY_LINK("Link is empty."),
        BROKEN_LINK("Broken link"),
        REQUEST_ERR("Error when making request."),
        SONG_NOT_FOUND("Songs not found."),
        ;
        String message;

        LCEType(String message) {
            this.message = message;
        }
    }
}
