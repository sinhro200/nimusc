package core.interfaces.tokenReceiver;

import core.InputParams;

import java.util.function.Consumer;

public interface TokenReceiver {

    void receiveToken(
            InputParams inputParams,
            Consumer<String> onAccessTokenReady,
            Consumer<TokenReceiverException> onError
    );
}
