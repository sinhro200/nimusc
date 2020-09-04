package usage.threadSleeper;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;


public class ThreadSleeper {
    private final static long DEF_TIMEOUT = 100;

    private ThreadSleeper() { }

    /**
     * do Thread.sleep until data is received
     * @param func
     * @param timeout
     * @param maxDelay
     * @param <D> - Data
     * @param <E> - Exception
     * @return
     * @throws E
     * @throws InterruptedException
     * @throws ThreadSleeperTimeoutException
     */
    public static <D,E extends Exception> D getData(
            BiConsumer<AtomicReference<D>, AtomicReference<E>> func,
            long timeout, long maxDelay
            ) throws E, InterruptedException, ThreadSleeperTimeoutException {
        AtomicReference<D> dataRef = new AtomicReference<>();
        AtomicReference<E> excRef = new AtomicReference<>();

        func.accept(dataRef, excRef);

        long delay_counter = 0;
        while (dataRef.get() == null && excRef.get()==null) {
            Thread.sleep(timeout);
            delay_counter+=timeout;
            if (delay_counter > maxDelay)
                throw new ThreadSleeperTimeoutException();
        }

        if (dataRef.get() != null)
            return dataRef.get();
        else
            throw excRef.get();

    }

    public static <D,E extends Exception> D getData(
            BiConsumer<AtomicReference<D>, AtomicReference<E>> func,
            long maxTimeout
    ) throws E, InterruptedException,ThreadSleeperTimeoutException {
        return getData(func,DEF_TIMEOUT,maxTimeout);
    }

}
