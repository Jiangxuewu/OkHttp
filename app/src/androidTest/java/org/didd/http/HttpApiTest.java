package org.didd.http;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jiangxuewu on 2017/12/11.
 */
public class HttpApiTest {

    class TestModel extends BaseModel {


        public TestModel(String interfaceName) {
            super(interfaceName);
        }

        @Override
        public HttpEntry toHttpEntry() {
            final HttpEntry httpEntry = new HttpEntry();
            httpEntry.setBaseUrl("http://www.bb-sz.com/ip.php");
            httpEntry.setType(Http.GET);
            httpEntry.setCallback(new IHttpCallback() {
                @Override
                public void result(HttpResponse httpResponse) {
                    if (null != httpResponse) {

                    }
                }
            });

            return httpEntry;
        }
    }

    @Test
    public void request() throws Exception {
        BaseModel model = new TestModel("");
        HttpApi.getInstance().request(model);
    }

}