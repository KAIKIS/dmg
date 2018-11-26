package com.github.kaikis.dmg;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoDBVerticle extends AbstractVerticle {

    private MongoClient client;
    private static final String COLLECTION_DATA = "data";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        client = MongoClient.createShared(vertx, configDB());
        EventBus bus = vertx.eventBus();
        bus.consumer("device.data", (Handler<Message<JsonObject>>) event -> {
            Data data = event.body().mapTo(Data.class);
            saveData(data);
        });
        startFuture.complete();
    }


    private void saveData(Data data) {
        client.save(COLLECTION_DATA, JsonObject.mapFrom(data), result -> {
            if (result.succeeded()) {
                System.out.println("success save data");
            } else {
                System.out.println("fail save data");
            }
        });
    }

    private JsonObject configDB() {
        JsonObject jb = new JsonObject();
        jb.put("host", "localhost");
        jb.put("port", 27017);
        jb.put("db_name", "m2m");
        return jb;
    }
}
