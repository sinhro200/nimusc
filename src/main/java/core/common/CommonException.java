package core.common;


public class CommonException extends Exception{
    protected String extra;

    public CommonException(String extra) {
        this.extra = extra;
    }


    public String getMessage() {
        return this.extra;
    }
}
