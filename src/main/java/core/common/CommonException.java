package core.common;


public class CommonException extends Exception {
    protected String extra;
    protected String typeText;

    public CommonException(CEType ceType,String extra) {
        this.typeText = ceType.name();
        this.extra = extra;
    }

    public CommonException(CEType ceType) {
        this(ceType,"");
    }

    public CommonException(String type,String extra) {
        this.typeText = type;
        this.extra = extra;
    }


    public String getMessage() {
        return typeText + (this.extra==null?"":this.extra);
    }

    public enum CEType{
        WRONG_HTTP_URL_PARAMETERS,
        ERR_WHILE_SENDING_REQUEST,
        ;
    }
}
