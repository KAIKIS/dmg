package com.github.kaikis.dmg.app;

import com.github.kaikis.dmg.Data;
import io.netty.util.CharsetUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import javafx.collections.ObservableList;

public class WebsocketClientVerticle extends AbstractVerticle {

    private ObservableList<DataProperty> datas;

    public WebsocketClientVerticle(ObservableList<DataProperty> datas) {
        this.datas = datas;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpClient client = vertx.createHttpClient();

        client.websocket(9090, "localhost", "/some-uri", websocket -> {
            websocket.handler(data -> {
                String s = data.toString(CharsetUtil.UTF_8);
                Data d = new JsonObject(s).mapTo(Data.class);
                datas.add(new DataProperty(d));
            });
        });
        startFuture.complete();
    }
}
