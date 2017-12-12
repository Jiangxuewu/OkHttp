package org.didd.http;


import android.text.TextUtils;
import android.util.Log;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;


/**
 * Created by Administrator on 2017/3/13.
 * <p>my http</p>
 */

public class Http {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int ERROR_NULL_URL_CODE = -2;
    public static final String ERROR_NULL_URL_MSG = "url is null.";
    public static final int ERROR_NOT_SUPPORT_CODE = -1;
    public static final String ERROR_NOT_SUPPORT_MSG = "not support cur type.";
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 120;
    public final static int WRITE_TIMEOUT = 120;

    private static final String TAG = "SkyHttp";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType X = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static Http mInstance;
    private static final boolean debug = true;

    public static Http getInstance() {
        synchronized (TAG) {
            if (null == mInstance) {
                mInstance = new Http();
            }
            return mInstance;
        }
    }

    public Http() {
    }

    private ExecutorService mRequestPool;

    /**
     * @param httpEntry http entry
     * @throws NullPointerException
     */
    public void request(final HttpEntry httpEntry) throws NullPointerException {
        if (null == httpEntry) throw new NullPointerException("HttpEntry is null!");
        if (null == mRequestPool)
            mRequestPool = Executors.newCachedThreadPool();
//        mRequestPool.submit(new Runnable() {
//            @Override
//            public void run() {
                try {
                    doRequest(httpEntry);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (null != httpEntry.getCallback()) {
                        HttpResponse err = new HttpResponse(1, "Net error");
                        httpEntry.getCallback().result(err);
                    }
                }
//            }
//        });
    }

    private void doRequest(HttpEntry httpEntry) throws IOException {
        if (null == httpEntry) throw new NullPointerException("HttpEntry is null!");
        if (httpEntry.getType() == GET) {
            doGet(httpEntry);
        } else if (httpEntry.getType() == POST) {
            doPost(httpEntry);
        } else {
            IHttpCallback callback = httpEntry.getCallback();
            if (null != callback) {
                callback.result(new HttpResponse(ERROR_NOT_SUPPORT_CODE, ERROR_NOT_SUPPORT_MSG));
            }
            return;
        }
    }

    private void doPost(HttpEntry httpEntry) throws IOException {
        doGet(httpEntry);
    }

    private Request.Builder handlerUrlAndHeader(HttpEntry httpEntry) {
        if (null == httpEntry) return null;
        String url = httpEntry.getBaseUrl();
        IHttpCallback callback = httpEntry.getCallback();
        if (TextUtils.isEmpty(url)) {
            if (null != callback)
                callback.result(new HttpResponse(ERROR_NULL_URL_CODE, ERROR_NULL_URL_MSG));
            return null;
        }
        Request.Builder builder = new Request.Builder();

        // add url
        builder.url(url);
        // add request header
        Map<String, String> header = httpEntry.getHeader();
        if (null != header && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                if (null != entry.getKey() && null != entry.getValue()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        //add body
        String bodyJson = httpEntry.getBodyJson();
        if (!TextUtils.isEmpty(bodyJson)) {//for point node post 
            RequestBody requestBody = RequestBody.create(JSON, bodyJson.replaceAll("\\\\", "").replace("\"[", "[")
                    .replace("]\"", "]"));
            builder.post(requestBody);
        }

        // add request body
        Map<String, String> body = httpEntry.getBody();
        if (null != body && body.size() > 0) {
            String bodyStr = mapToString(body);
            if (!TextUtils.isEmpty(bodyStr)) {
                RequestBody requestBody = RequestBody.create(X, bodyStr);
                builder.post(requestBody);
            }
        }
        return builder;
    }

    public static String mapToString(Map<String, String> map) {
        if (null == map) return null;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        return sb.toString();
    }

    private void doGet(HttpEntry httpEntry) throws IOException {
        Request.Builder builder = handlerUrlAndHeader(httpEntry);
        if (null == builder) {
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        Request request = builder.build();
        Response response = client.newCall(request).execute();

        IHttpCallback callback = httpEntry.getCallback();
        if (null != callback) {
            HttpResponse httpResponse = convertHttpResponse(response, httpEntry);
            if (debug) Log.d(TAG, httpResponse.log());
            callback.result(httpResponse);
        }
    }

    private HttpResponse convertHttpResponse(Response response, HttpEntry entry) {
        return new HttpResponse(response.code(), response.message(), entry, convertHttpResponseHeader(response.headers()), convertHttpResponseBody(response.body()));
    }

    private HttpResponseBody convertHttpResponseBody(ResponseBody body) {
        MediaType contentType = body.contentType();
        try {
            return new HttpResponseBody(body.byteStream(), contentType != null ? contentType.charset(Util.UTF_8) : Util.UTF_8, body.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponseBody(body.byteStream(), contentType != null ? contentType.charset(Util.UTF_8) : Util.UTF_8);
    }

    private HttpResponseHeader convertHttpResponseHeader(Headers headers) {
        return new HttpResponseHeader(headers.toMultimap());
    }
}
