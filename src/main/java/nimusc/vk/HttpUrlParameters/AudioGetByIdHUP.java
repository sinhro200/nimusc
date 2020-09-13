package nimusc.vk.HttpUrlParameters;

import lombok.Builder;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import okhttp3.HttpUrl;

import java.util.List;

@Builder
public class AudioGetByIdHUP implements HttpUrlParameters {
    private List<String> ids;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        if (ids == null || ids.isEmpty())
            throw new NimuscException(CommonNE.WRONG_HTTP_URL_PARAMETERS,"Ids is null or empty");
        urlBuilder.addQueryParameter("audios",String.join(",",ids));
    }
}
