package nimusc.core.request;

import nimusc.core.authorization.Authorization;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.java.Log;
import nimusc.core.exceptions.AuthorizationNE;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Builder
@Log
public class RequestEntity implements RequestSender{

    private int timeoutSeconds = 3;

    private final OkHttpClient httpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .build();

    @NonNull
    private String requestUrl;

    private HttpUrlParameters commonUrlParams;

    private RequestHeaderParameters commonRequestHeaderParams;

    private Request baseRequest(HttpUrlParameters userParams, Authorization authorization) throws NimuscException{
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(requestUrl).newBuilder();
        if (commonUrlParams !=null)
            commonUrlParams.applyToHttpBuilder(urlBuilder);


        if (userParams!=null)
            userParams.applyToHttpBuilder(urlBuilder);

        if (authorization!=null)
            authorization.applyToHttpBuilder(urlBuilder);

        urlBuilder
                .build();

//        log.info("_Trying to execute 'get' to : "+urlBuilder.toString());

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.toString());
        if (commonRequestHeaderParams != null) {
            commonRequestHeaderParams.applyToRequestBuilder(requestBuilder);
        }

        if (authorization!=null)
            authorization.applyToRequestBuilder(requestBuilder);

        Request r =  requestBuilder.build();
        log.info("Request builded to url : " + r.url());
        return r;
    }

    private String getValidatedResponse(Response response) throws NimuscException{
        if (response.isSuccessful()){
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                log.info("Error while sending request. Response body is null");
                throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST, "Response body is null");
            }

            try {
                String resp = responseBody.string();
                log.info("Success while sending request. Response body : " + resp);

                return resp;
            } catch (IOException e) {
                throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,e.getMessage());
            }
        }else{
            if (response.code() == 401) {
                log.info("Error while sending request. 401 unauthorized");
                throw new NimuscException(AuthorizationNE.UNAUTHORIZED);
            }

            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,"Error code :" + response.code());
        }
    }

    @Override
    public void doRequestAsync(HttpUrlParameters userParams, Authorization authorization, Consumer<String> onResponse, Consumer<NimuscException> onError){

        Request request = null;
        try {
            request = baseRequest(userParams, authorization);
        } catch (NimuscException e) {
            onError.accept(e);
            return;
        }

        log.info("Send request : "+ request.url().toString());

        httpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        log.info("Error while sending request : " + e.getMessage() + ". "+e.getStackTrace());
                        onError.accept(new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST));
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            String resp = getValidatedResponse(response);
                            onResponse.accept(resp);
                        } catch (NimuscException e) {
                            onError.accept(e);
                        }
                    }
                }
        );
    }

    @Override
    public String doRequestSync(HttpUrlParameters userParams, Authorization authorization) throws NimuscException {
        Request request = baseRequest(
                userParams,authorization
        );
        try {
            Response response = httpClient.newCall(request).execute();
            return getValidatedResponse(response);
        } catch (IOException e) {
            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,e.getMessage());
        }
    }
}
