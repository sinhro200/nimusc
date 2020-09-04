package usage.threadSleeper;

public class ThreadSleeperTimeoutException extends Exception{
    public ThreadSleeperTimeoutException(String message) {
        super(message);
    }

    public ThreadSleeperTimeoutException() {
    }
}
