package com.javahelp.frontend.gateway;


import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParsingException;

/**
 * Abstract class for making requests to a REST API
 * Exposes asynchronous methods for making REST API calls, either through callbacks or futures.
 *
 * @param <T> the type of {@link RESTAPIGateway} result to expect
 */
public abstract class RESTAPIGateway<T> {

    /**
     * Make a request to a REST API and get a response
     * @param request {@link SimpleHttpRequest} to make
     * @param callback {@link FutureCallback} to execute
     * @return {@link Future} for the request result
     */
    protected Future<RESTAPIGatewayResponse<T>> getResponse(SimpleHttpRequest request,
                                                         FutureCallback<RESTAPIGatewayResponse<T>> callback) {
        FutureCallback<InternalRESTGatewayResponse> passedCallback =
                callback == null ? null : new FutureCallback<InternalRESTGatewayResponse>() {
                    @Override
                    public void completed(InternalRESTGatewayResponse result) {
                        callback.completed(fromInternal(result));
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
        Future<InternalRESTGatewayResponse> future = this.getInternalResponse(request, passedCallback);
        return new Future<RESTAPIGatewayResponse<T>>() {
            @Override
            public boolean cancel(boolean b) {
                return future.cancel(b);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public RESTAPIGatewayResponse<T> get() throws ExecutionException, InterruptedException {
                return fromInternal(future.get());
            }

            @Override
            public RESTAPIGatewayResponse<T> get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return fromInternal(future.get(l, timeUnit));
            }
        };
    }

    /**
     * Converts the given {@link InternalRESTGatewayResponse} to a {@link RESTAPIGatewayResponse}
     *
     * @param response {@link InternalRESTGatewayResponse} to convert
     * @return the resulting {@link RESTAPIGatewayResponse<T>}
     */
    protected abstract RESTAPIGatewayResponse<T> fromInternal(InternalRESTGatewayResponse response);

    /**
     * Asynchronously gets a {@link InternalRESTGatewayResponse}
     *
     * @param request  {@link SimpleHttpRequest} to get response to
     * @param callback {@link FutureCallback} to call, or null if no callback is desired
     * @return {@link Future} for {@link InternalRESTGatewayResponse}
     */
    private Future<InternalRESTGatewayResponse> getInternalResponse(SimpleHttpRequest request, FutureCallback<InternalRESTGatewayResponse> callback) {
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
     * Wraps a {@link InternalRESTGatewayResponse} {@link Future} around a {@link SimpleHttpResponse} {@link Future}
     *
     * @param responseFuture {@link InternalRESTGatewayResponse} {@link Future} to wrap with
     * @return wrapped {@link InternalRESTGatewayResponse} future
     */
    private Future<InternalRESTGatewayResponse> wrapFuture(Future<SimpleHttpResponse> responseFuture) {
        return new Future<InternalRESTGatewayResponse>() {
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
            public InternalRESTGatewayResponse get() throws ExecutionException, InterruptedException {
                return fromSimpleResponse(responseFuture.get());
            }

            @Override
            public InternalRESTGatewayResponse get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return fromSimpleResponse(responseFuture.get(l, timeUnit));
            }
        };
    }

    /**
     * Wraps a {@link SimpleHttpResponse} {@link FutureCallback} around a {@link InternalRESTGatewayResponse}
     * {@link FutureCallback}
     *
     * @param callback {@link InternalRESTGatewayResponse} {@link FutureCallback} to wrap around
     * @return the {@link SimpleHttpResponse} callback
     */
    private FutureCallback<SimpleHttpResponse> wrapCallback(FutureCallback<InternalRESTGatewayResponse> callback) {
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
     * Get a {@link InternalRESTGatewayResponse} from a {@link SimpleHttpResponse}
     *
     * @param response {@link SimpleHttpResponse} to convert
     * @return resulting {@link InternalRESTGatewayResponse}
     */
    private static InternalRESTGatewayResponse fromSimpleResponse(SimpleHttpResponse response) {
        String body = response.getBodyText();
        int responseCode = response.getCode();
        return new InternalRESTGatewayResponse(body, responseCode);
    }

    /**
     * Response from a {@link RESTAPIGateway},
     * bundles a response code and a {@link jakarta.json.JsonObject} body
     */
    protected static class InternalRESTGatewayResponse {

        private boolean successfullyParsed;
        private boolean shouldHaveBody;
        private JsonObject body;
        private int responseCode;

        /**
         * Creates a new {@link InternalRESTGatewayResponse}
         *
         * @param body         {@link String} body to use
         * @param responseCode response code received
         */
        public InternalRESTGatewayResponse(String body, int responseCode) {
            this.responseCode = responseCode;
            successfullyParsed = true;
            shouldHaveBody = body != null;
            try {
                if (shouldHaveBody) {
                    this.body = Json.createReader(new StringReader(body)).readObject();
                }
            } catch (JsonParsingException e) {
                successfullyParsed = false;
            }
        }

        /**
         * @return whether the response successfully parsed every body {@link String} it was passed
         */
        public boolean isSuccessfullyParsed() {
            return successfullyParsed;
        }

        /**
         * @return whether the response was passed a body {@link String}
         */
        public boolean shouldHaveBody() {
            return shouldHaveBody;
        }

        /**
         * @return whether the response is valid (i.e. if it should have a body, was it parsed?)
         */
        public boolean validResponse() {
            return !shouldHaveBody || successfullyParsed;
        }

        /**
         * @return the response body as a {@link JsonObject}
         */
        public JsonObject getBody() {
            return body;
        }

        /**
         * @return response code for this {@link InternalRESTGatewayResponse}
         */
        public int getResponseCode() {
            return this.responseCode;

        }
    }

}
