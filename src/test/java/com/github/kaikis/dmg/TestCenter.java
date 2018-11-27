package com.github.kaikis.dmg;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.io.IOException;

public class TestCenter {

    @Test
    public void test() throws IOException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MqttClientVerticle());
        System.in.read();
    }

    @Test
    public void test2() {

        String s = "{\"deviceId\":1,\"dataId\":1,\"nodeId\":1,\"dataType\":\"int\",\"data\":\"123\",\"createTime\":1543247953078}";

//        Data d = new JsonObject(s).mapTo();
    }
}
