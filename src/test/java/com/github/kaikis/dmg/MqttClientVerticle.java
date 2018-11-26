package com.github.kaikis.dmg;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Date;

/**
 * @author Kai
 * Date : 2018/1/30
 */
public class MqttClientVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MqttClientVerticle.class);
    private MqttClient client;
    private static final String MQTT_TOPIC = "/my_topic";
    private static final String MQTT_MESSAGE = "Hello Vert.x MQTT Client";
    private static final String BROKER_HOST = "localhost";
    private static final int BROKER_PORT = 1883;


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MqttClientOptions options = new MqttClientOptions().setKeepAliveTimeSeconds(10);

        client = MqttClient.create(vertx, options);


        // handler will be called when we have a message in topic we subscribing for
        client.publishHandler(publish -> {
            System.out.println("Just received message on [" + publish.topicName() + "] payload [" + publish.payload().toString(Charset.defaultCharset()) + "] with QoS [" + publish.qosLevel() + "]");
        });

        // handle response on subscribe request
        client.subscribeCompletionHandler(h -> {
            System.out.println("Receive SUBACK from server with granted QoS : " + h.grantedQoSLevels());

            // let's publish a message to the subscribed topic


            // unsubscribe from receiving messages for earlier subscribed topic
//            vertx.setTimer(5000, l -> client.unsubscribe(MQTT_TOPIC));
            vertx.setPeriodic(1000, l -> {
                client.publish(
                        MQTT_TOPIC,
                        Buffer.buffer(JsonObject.mapFrom(makeData()).toString()),
                        MqttQoS.AT_MOST_ONCE,
                        false,
                        false,
                        s -> System.out.println("Publish sent to a server "));
            });
        });

        // handle response on unsubscribe request
        client.unsubscribeCompletionHandler(h -> {
            System.out.println("Receive UNSUBACK from server");
            vertx.setTimer(5000, l ->
                    // disconnect for server
                    client.disconnect(d -> System.out.println("Disconnected form server"))
            );
        });

        // connect to a server
        client.connect(BROKER_PORT, BROKER_HOST, ch -> {
            if (ch.succeeded()) {
                log.info("Connect to Mqtt server...");
                client.subscribe(MQTT_TOPIC, 0);
            } else {
                log.info("Failed to connect to a Mqtt server...");
                ch.cause().printStackTrace();
            }
        });
        startFuture.complete();
    }

    private Data makeData(){
        Data data = new Data();
        data.setDeviceId(1);
        data.setDataId(1);
        data.setNodeId(1);
        data.setData("123");
        data.setCreateTime(new Date());
        data.setDataType("int");
        return data;
    }

    @Override
    public void stop() throws Exception {
        client.disconnect(ar -> {
            if (ar.succeeded()) {
                log.info("Mqtt Client has closed");
            } else {
                log.info("Mqtt Client failed to close");
                ar.cause().printStackTrace();
            }
        });
    }
}
