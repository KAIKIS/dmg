package com.github.kaikis.dmg;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import org.junit.Test;

import java.io.IOException;

public class WebsocketTest {


    @Test
    public void testServer() throws IOException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Client());
        System.in.read();
    }


    class Server extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            HttpServer server = vertx.createHttpServer();
            server.websocketHandler(web -> {
                System.out.println("Connected");
                vertx.setPeriodic(1000, event -> {
                    web.writeTextMessage("123");
                });
            }).listen(9090);
        }
    }

    class Client extends AbstractVerticle{
        @Override
        public void start() throws Exception {
            HttpClient client = vertx.createHttpClient();

            client.websocket(9090, "localhost", "/some-uri", websocket -> {
                websocket.handler(data -> {
                    System.out.println("Received data " + data.toString("ISO-8859-1"));
//                    client.close();
                });
                websocket.writeTextMessage("Hello world");
            });
        }
    }
}
