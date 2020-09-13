package nimusc.core.request;

import nimusc.core.common.exception.NimuscException;

import java.util.function.Consumer;

public interface RequestSender {
    void send(HttpUrlParameters userParams, Consumer<String> onResponse, Consumer<NimuscException> onError);
}
