package nimusc.core.common.exception;


public class NimuscException extends Exception {
    protected String extra;
    protected NimuscExceptionType type;

    public NimuscException(NimuscExceptionType type, String extra) {
        this.type = type;
        this.extra = extra;
    }

    public NimuscException(NimuscExceptionType type) {
        this(type,"");
    }


    public NimuscExceptionType getType() {
        return type;
    }

    public String getMessage() {
        return type.getMessage() + (this.extra==null?"":". "+this.extra);
    }
}
