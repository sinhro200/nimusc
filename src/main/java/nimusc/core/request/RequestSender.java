package nimusc.core.request;

import nimusc.core.authorization.Authorization;
import nimusc.core.common.exception.NimuscException;

import java.util.function.Consumer;

public interface RequestSender {
    void doRequestAsync(
            HttpUrlParameters userParams,
            Authorization authorization,
            Consumer<String> onResponse,
            Consumer<NimuscException> onError
    );

    String doRequestSync(
            HttpUrlParameters userParams,
            Authorization authorization
    ) throws NimuscException;
}
