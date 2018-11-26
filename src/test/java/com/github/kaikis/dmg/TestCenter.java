package com.github.kaikis.dmg;

import io.vertx.core.Vertx;
import org.junit.Test;

import java.io.IOException;

public class TestCenter {

    @Test
    public void test() throws IOException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MqttClientVerticle());
        System.in.read();
    }
}
