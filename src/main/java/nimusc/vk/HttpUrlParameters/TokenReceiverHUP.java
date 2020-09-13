package nimusc.vk.HttpUrlParameters;

import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import lombok.Builder;
import okhttp3.HttpUrl;

@Builder
public class TokenReceiverHUP implements HttpUrlParameters {
    private String login;
    private String password;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        if (login == null || password == null || login.isEmpty()||password.isEmpty())
            throw new NimuscException(CommonNE.WRONG_HTTP_URL_PARAMETERS,"Login or Password is empty or null");
        urlBuilder
                .addQueryParameter("username", login)
                .addQueryParameter("password", password);

    }
}
