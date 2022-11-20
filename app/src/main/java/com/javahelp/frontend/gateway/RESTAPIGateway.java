package com.javahelp.frontend.gateway;


import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Abstract class for making requests to a REST API.
 * <p>
 * Exposes asynchronous methods for making REST API calls, either through callbacks or futures.
 */
public abstract class RESTAPIGateway {

    /**
     * Asynchronously gets a {@link RESTGatewayResponse}
     *
     * @param request  {@link SimpleHttpRequest} to get response to
     * @param callback {@link FutureCallback} to call, or null if no callback is desired
     * @return {@link Future} for {@link RESTGatewayResponse}
     */
    protected Future<RESTGatewayResponse> getResponse(SimpleHttpRequest request, FutureCallback<RESTGatewayResponse> callback) {
        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();

            FutureCallback<SimpleHttpResponse> passedCallback = null;

            if (callback != null) {
                passedCallback = wrapCallback(callback);
            }

            Future<SimpleHttpResponse> responseFuture = client.execute(request, passedCallback);

            return wrapFuture(responseFuture);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open HTTP client");
        }
    }

    /**
     * Wraps a {@link RESTGatewayResponse} {@link Future} around a {@link SimpleHttpResponse} {@link Future}
     *
     * @param responseFuture {@link RESTGatewayResponse} {@link Future} to wrap with
     * @return wrapped {@link RESTGatewayResponse} future
     */
    private Future<RESTGatewayResponse> wrapFuture(Future<SimpleHttpResponse> responseFuture) {
        return new Future<RESTGatewayResponse>() {
            @Override
            public boolean cancel(boolean b) {
                return responseFuture.cancel(b);
            }

            @Override
            public boolean isCancelled() {
                return responseFuture.isCancelled();
            }

            @Override
            public boolean isDone() {
                return responseFuture.isDone();
            }

            @Override
            public RESTGatewayResponse get() throws ExecutionException, InterruptedException {
                return fromSimpleResponse(responseFuture.get());
            }

            @Override
            public RESTGatewayResponse get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return fromSimpleResponse(responseFuture.get(l, timeUnit));
            }
        };
    }

    /**
     * Wraps a {@link SimpleHttpResponse} {@link FutureCallback} around a {@link RESTGatewayResponse}
     * {@link FutureCallback}
     *
     * @param callback {@link RESTGatewayResponse} {@link FutureCallback} to wrap around
     * @return the {@link SimpleHttpResponse} callback
     */
    private FutureCallback<SimpleHttpResponse> wrapCallback(FutureCallback<RESTGatewayResponse> callback) {
        FutureCallback<SimpleHttpResponse> passedCallback;
        passedCallback = new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse result) {
                callback.completed(fromSimpleResponse(result));
            }

            @Override
            public void failed(Exception ex) {
                callback.failed(ex);
            }

            @Override
            public void cancelled() {
                callback.cancelled();
            }
        };
        return passedCallback;
    }

    /**
     * Get a {@link RESTGatewayResponse} from a {@link SimpleHttpResponse}
     *
     * @param response {@link SimpleHttpResponse} to convert
     * @return resulting {@link RESTGatewayResponse}
     */
    private static RESTGatewayResponse fromSimpleResponse(SimpleHttpResponse response) {
        String body = response.getBodyText();
        int responseCode = response.getCode();
        return new RESTGatewayResponse(body, responseCode);
    }

}
