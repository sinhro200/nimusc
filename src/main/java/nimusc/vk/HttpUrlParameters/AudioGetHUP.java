package nimusc.vk.HttpUrlParameters;

import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import lombok.Builder;
import okhttp3.HttpUrl;

@Builder
public class AudioGetHUP implements HttpUrlParameters {
    private String ownerId;
    private String albumId;
    private Integer count = null;
    private Integer offset = null;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        if (ownerId == null && albumId==null)
            throw new NimuscException(CommonNE.WRONG_HTTP_URL_PARAMETERS,"ownerId and albumId is null or empty");
        urlBuilder.addQueryParameter("owner_id",ownerId);
        urlBuilder.addQueryParameter("album_id",albumId);
        if (count!=null)
            urlBuilder.addQueryParameter("count", String.valueOf(count));
        if (offset!=null)
            urlBuilder.addQueryParameter("offset", String.valueOf(offset));
    }
}
