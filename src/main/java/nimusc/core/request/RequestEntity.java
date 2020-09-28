package nimusc.core.request;

import nimusc.core.authorization.Authorization;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.java.Log;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

@Builder
@Log
public class RequestEntity implements RequestSender{

    private final OkHttpClient httpClient = new OkHttpClient();

    @NonNull
    private String requestUrl;

    private HttpUrlParameters commonUrlParams;

    private RequestHeaderParameters commonRequestHeaderParams;

    @Override
    public void send(HttpUrlParameters userParams, Authorization authorization, Consumer<String> onResponse, Consumer<NimuscException> onError){
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(requestUrl).newBuilder();
        if (commonUrlParams !=null)
            try{
                commonUrlParams.applyToHttpBuilder(urlBuilder);
            }catch (NimuscException ce){
                onError.accept(ce);
                return;
            }

        if (userParams!=null)
            try{
                userParams.applyToHttpBuilder(urlBuilder);
            }catch (NimuscException ce){
                onError.accept(ce);
                return;
            }

        if (authorization!=null)
            authorization.applyToHttpBuilder(urlBuilder);

        urlBuilder
                .build();

//        log.info("_Trying to execute 'get' to : "+urlBuilder.toString());

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.toString());
        if (commonRequestHeaderParams != null) {
            try {
                commonRequestHeaderParams.applyToRequestBuilder(requestBuilder);
            } catch (NimuscException e) {
                onError.accept(e);
                return;
            }
        }

        if (authorization!=null)
            authorization.applyToRequestBuilder(requestBuilder);


        log.info("Send request : "+ urlBuilder.toString());
        Request request = requestBuilder.build();
        Call call = httpClient.newCall(request);
        call.enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        log.info("Error while sending request : " + e.getMessage() + ". "+e.getStackTrace());
                        onError.accept(new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST));
                        return;
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            ResponseBody responseBody = response.body();
                            if (responseBody == null) {
                                log.info("Error while sending request. Response body is null");
                                onError.accept(new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST, "Response body is null"));
                                return;
                            }
                            String resp = responseBody.string();
                            log.info("Success while sending request. Response body : " + resp);
                            onResponse.accept(resp);

                        }else{
                            log.info("Error while sending request. ");
                            onError.accept(new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST));
                            return;
                        }

                    }
                }
        );
    }
}
