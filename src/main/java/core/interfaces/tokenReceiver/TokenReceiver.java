package core.interfaces.tokenReceiver;

import core.request.HttpUrlParameters;

import java.util.function.Consumer;

public interface TokenReceiver {

    void receiveToken(
            HttpUrlParameters userParams,
            Consumer<String> onAccessTokenReady,
            Consumer<TokenReceiverException> onError
    );
}
