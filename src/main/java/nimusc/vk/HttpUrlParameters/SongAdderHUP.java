package nimusc.vk.HttpUrlParameters;

import lombok.Builder;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import okhttp3.HttpUrl;

@Builder
public class SongAdderHUP implements HttpUrlParameters {
    private String ownerId;
    private String audioId;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        if (ownerId == null && audioId==null)
            throw new NimuscException(CommonNE.WRONG_HTTP_URL_PARAMETERS,"ownerId and audioId is null or empty");
        urlBuilder.addQueryParameter("owner_id",ownerId);
        urlBuilder.addQueryParameter("audio_id",audioId);

    }
}
