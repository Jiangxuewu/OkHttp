package org.didd.http;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/13.
 */

public class HttpEntry {
    private int type;//POST GET
    private String baseUrl;//base url
    private IHttpCallback callback;//call back

    private Map<String, String> header;

    @Deprecated
    private Map<String, String> body;// post body

    private String bodyJson;// body json

    public String getBodyJson() {
        return bodyJson;
    }

    public void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }

    @Deprecated
    public Map<String, String> getBody() {
        return body;
    }

    @Deprecated
    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public IHttpCallback getCallback() {
        return callback;
    }

    public int getType() {
        return type;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setCallback(IHttpCallback callback) {
        this.callback = callback;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String log() {
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------Request Start-------------------------------------------").append("\n");
        sb.append("            ").append((type == Http.GET ? "GET":(type == Http.POST ? "POST": "unknown"))).append(" ").append(baseUrl).append("\n");
        sb.append("\n");
        if (null != header) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                sb.append("            ").append("    ").append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
        }
        sb.append("\n");
        if (null != bodyJson) {
            sb.append("\n").append("            ").append("    ").append(bodyJson).append("\n");
        }
        sb.append("\n");
        if (null != body){
            sb.append("\n").append("            ").append("    ").append(body.toString()).append("\n");
        }
        sb.append("---------------------------------Request  End-------------------------------------------").append("\n");
        return sb.toString();
    }
}
