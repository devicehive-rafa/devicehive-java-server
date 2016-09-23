package com.devicehive.shim.kafka.fixture;

import com.devicehive.shim.api.Body;

public class TestRequestBody extends Body {

    private String body;

    public TestRequestBody() {
        super("test_request");
    }

    public TestRequestBody(String body) {
        super("test_request");
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}