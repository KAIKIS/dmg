package com.github.kaikis.dmg;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketServerVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(WebsocketServerVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(web -> {
            log.info("client connected");

            vertx.eventBus().consumer("device.data", (Handler<Message<JsonObject>>) event -> {
                web.writeTextMessage(event.body().toString());
            });
        }).listen(9090);
        startFuture.complete();
    }
}
