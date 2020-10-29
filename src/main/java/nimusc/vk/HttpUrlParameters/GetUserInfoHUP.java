package nimusc.vk.HttpUrlParameters;

import lombok.Builder;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import okhttp3.HttpUrl;

@Builder
public class GetUserInfoHUP implements HttpUrlParameters {
    String user_id;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        urlBuilder.addQueryParameter("user_ids",user_id);
    }
}
