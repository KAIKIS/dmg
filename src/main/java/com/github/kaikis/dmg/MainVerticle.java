package com.github.kaikis.dmg;

import com.google.common.collect.Lists;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);


    public static void main(String[] args) throws IOException {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new MainVerticle());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<String> dbVerticleDeployment = Future.future();
        DeploymentOptions options = new DeploymentOptions();
//        dbVerticleDeployment.complete();
//        options.setConfig(new JsonObject().put("clean", clean));
        vertx.deployVerticle(new MongoDBVerticle(), options, dbVerticleDeployment.completer());
        dbVerticleDeployment.compose(t -> {
            Future<String> mqttServerVerticleDeployment = Future.future();
//            Future<String> mqttClientVerticleDeployment = Future.future();
            CompositeFuture compositeFuture = CompositeFuture.all(Lists.newArrayList(
//                    mqttClientVerticleDeployment,
                    mqttServerVerticleDeployment
            ));
//            vertx.deployVerticle(new HttpInboundVerticle(), httpVerticleDeployment.completer());
            vertx.deployVerticle(new MqttServerVerticle(), mqttServerVerticleDeployment.completer());
//            vertx.deployVerticle(new MqttClientVerticle(), mqttClientVerticleDeployment.completer());
            return compositeFuture;
        }).setHandler(result -> {
            if (result.succeeded()) {
                log.info("m2mCloud has started...");
                startFuture.complete();
            } else {
                log.info("m2mCloud failed to started...");
                startFuture.fail(result.cause());
            }
        });
    }
}
