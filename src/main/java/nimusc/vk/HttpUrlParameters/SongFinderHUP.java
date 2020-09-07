package nimusc.vk.HttpUrlParameters;

import nimusc.core.common.CommonException;
import nimusc.core.request.HttpUrlParameters;
import lombok.Builder;
import okhttp3.HttpUrl;

/**
 * From original api :
 * q	                    -   Text to search for
 * count (optional)	        -   Maximum number of audios to return
 * offset (optional)	    -   Offset to skip that number of audios
 * performer_only (optional)-   1 — search only for performer, 0 or not specified — search for performer and song name
 * auto_complete (optional)	-   1 — fix errors in query (Огыешт => Justin), 0 or not specified — don't fix
 */
@Builder
public class SongFinderHUP implements HttpUrlParameters {
    private String query;
    private Integer count;
    private Integer offset;
    private Boolean performerOnly;
    private Boolean autoComplete;

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws CommonException {
        if (query == null || query.isEmpty())
            throw new CommonException(CommonException.CEType.WRONG_HTTP_URL_PARAMETERS,"Query is null or empty");
//        try {
//            urlBuilder.addQueryParameter("q",  URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
            urlBuilder.addQueryParameter("q",  query);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        if (count!=null)
            urlBuilder.addQueryParameter("count", String.valueOf(count));
        if (offset!=null)
            urlBuilder.addQueryParameter("offset", String.valueOf(offset));
        if (performerOnly!=null)
            urlBuilder.addQueryParameter("performer_only",performerOnly?"1":"0");
        if (autoComplete!=null)
            urlBuilder.addQueryParameter("auto_complete", autoComplete?"1":"0");
    }
}
