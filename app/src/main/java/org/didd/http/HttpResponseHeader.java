package org.didd.http;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */

public class HttpResponseHeader {
    private final Map<String, List<String>> map;

    @Deprecated
    private final String[] namesAndValues = null;

    @Deprecated
    public HttpResponseHeader(String[] namesAndValues, Map<String, List<String>> map) {
        this.map = map;
    }

    public HttpResponseHeader(Map<String, List<String>> map) {
        this.map = map;
    }

    public Map<String, List<String>> getMap() {
        return map;
    }
}
