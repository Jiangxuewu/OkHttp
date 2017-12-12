package org.didd.http;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/3/14.
 */

public class HttpResponseBody {

    private final InputStream inputStream;
    private final Charset charset;

    private String string;


    public HttpResponseBody(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }

    public HttpResponseBody(InputStream inputStream, Charset charset, String string) {
        this.inputStream = inputStream;
        this.charset = charset;
        this.string = string;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
