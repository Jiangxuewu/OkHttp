package org.didd.http;

import android.text.TextUtils;

/**
 * Created by Jiangxuewu on 2017/12/4.
 */

public class HttpApi {
    private final String url = "";
    private static final String TAG = "HttpApi";
    private static HttpApi mInstance;

    public static HttpApi getInstance() {
        synchronized (TAG) {
            if (null == mInstance) {
                mInstance = new HttpApi();
            }
            return mInstance;
        }
    }

    public HttpApi() {

    }

    public void request(BaseModel model) {
        if (null == model) return;
        HttpEntry entry = model.toHttpEntry();
        if (null == entry) return;
        String allUrl = (entry.getBaseUrl().startsWith("http") ? entry.getBaseUrl() : url + entry.getBaseUrl());
        if (entry.getType() == Http.GET) {
            if (TextUtils.isEmpty(Http.mapToString(entry.getBody()))){
                entry.setBaseUrl(allUrl/*.endsWith("?") ? allUrl : allUrl + "?" */);
            } else {
                entry.setBaseUrl(allUrl + "?" + Http.mapToString(entry.getBody()));
            }
            entry.setBody(null);
        } else
            entry.setBaseUrl(allUrl/* + "?"*/);
        Http.getInstance().request(entry);
    }
}
