package nimusc.vk.HttpUrlParameters;

import nimusc.core.common.CommonException;
import nimusc.core.request.HttpUrlParameters;
import lombok.Builder;
import okhttp3.HttpUrl;

import java.util.List;

@Builder
public class LinkConvertingHUP implements HttpUrlParameters {
    private List<String> ids;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws CommonException {
        if (ids == null || ids.isEmpty())
            throw new CommonException(CommonException.CEType.WRONG_HTTP_URL_PARAMETERS,"Ids is null or empty");
        urlBuilder.addQueryParameter("audios",String.join(",",ids));

    }
}
