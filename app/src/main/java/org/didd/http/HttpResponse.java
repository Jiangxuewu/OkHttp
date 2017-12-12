package org.didd.http;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/13.
 */

public class HttpResponse {

    public int code;
    public String message;

    private HttpEntry entry;
    private HttpResponseHeader header;
    private HttpResponseBody body;

    public HttpResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpResponse(int code, String message, HttpEntry entry, HttpResponseHeader header, HttpResponseBody body) {
        this.code = code;
        this.message = message;
        this.entry = entry;
        this.header = header;
        this.body = body;
    }

    public HttpEntry getEntry() {
        return entry;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public HttpResponseBody getBody() {
        return body;
    }

    public String log() {
        StringBuffer sb = new StringBuffer();
        sb.append(entry.log());
        sb.append("\n");
        sb.append("-------------------------------Response Start------------------------------------------------").append("\n");
        sb.append("            ").append(code).append(" ").append((null == message ? "" : message)).append("\n");
        int len = 0;
        String bank = "";
        int size = 0;
        sb.append("\n");
        if (null != header && null != header.getMap()) {
            for (Map.Entry<String, List<String>> entry : header.getMap().entrySet()) {
                sb.append("            ").append("    ").append(entry.getKey()).append(":");
                len = (entry.getKey() + ":").length();
                bank = getBanks(len);
                size = (null == entry.getValue() ? 0 : entry.getValue().size());
                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        sb.append("            ").append("    ").append(bank);
                    }
                    sb.append(entry.getValue().get(i)).append("\n");
                }
            }
        }
        sb.append("\n");
        if (null != body) {
            sb.append("\n").append("            ").append("    ").append(body.getCharset().displayName()).append("\n");
            sb.append("            ").append("    ").append(body.getString()).append("\n");
        }
        sb.append("-------------------------------Response  End--------------------------------------------------").append("\n");
        return sb.toString();
    }

    private String getBanks(int len) {
        if (len < 0) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
