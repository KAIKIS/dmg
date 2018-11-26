package com.github.kaikis.dmg;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MqttServerVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(MqttServerVerticle.class);

    private MqttServer mqttServer;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MqttServerOptions options = new MqttServerOptions();
//        options.
        mqttServer = MqttServer.create(vertx);
        mqttServer.endpointHandler(this::handler)
                .listen(ar -> {
                    if (ar.succeeded()) {
                        log.info("Mqtt server started on port {}", ar.result().actualPort());
                    } else {
                        log.info("Mqtt server failed to start");
                        ar.cause().printStackTrace();
                    }
                    startFuture.complete();
                });
    }

    private void handler(MqttEndpoint mqttEndpoint) {
        mqttEndpoint.accept(false);
        log.info("Mqtt client {} request to connect", mqttEndpoint.clientIdentifier());
        if (mqttEndpoint.auth() != null) {
            System.out.println("[username = " + mqttEndpoint.auth().userName() + ", password = " + mqttEndpoint.auth().password() + "]");
        }
        if (mqttEndpoint.will() != null) {
            System.out.println("[will topic = " + mqttEndpoint.will().willTopic() + " msg = " + mqttEndpoint.will().willMessage() +
                    " QoS = " + mqttEndpoint.will().willQos() + " isRetain = " + mqttEndpoint.will().isWillRetain() + "]");
        }
        //处理Subscribe请求--订阅主题
        mqttEndpoint.subscribeHandler(msg -> {
            List<MqttQoS> grantedQosLevels = new ArrayList<>();
            for (MqttTopicSubscription s : msg.topicSubscriptions()) {
                System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
                grantedQosLevels.add(s.qualityOfService());
            }
            // ack the subscriptions request
            mqttEndpoint.subscribeAcknowledge(msg.messageId(), grantedQosLevels);
        });
        //处理Unsubscribe请求
        mqttEndpoint.unsubscribeHandler(msg -> {

            for (String t : msg.topics()) {
                System.out.println("Unsubscription for " + t);
            }
            // ack the subscriptions request
            mqttEndpoint.unsubscribeAcknowledge(msg.messageId());
        });

        //处理publish请求--接收到请求
        mqttEndpoint.publishHandler(msg -> {
            log.info("Receive publish message {} in topic {}", msg.payload().toString(), msg.topicName());
            if (msg.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                mqttEndpoint.publishAcknowledge(msg.messageId());
            } else if (msg.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                mqttEndpoint.publishRelease(msg.messageId());
            }

//            DeliveryOptions options = new DeliveryOptions();
//            options.addHeader(DATABASE_ACTION, DATABASE_ADD_DATA);
            vertx.eventBus().publish("device.data", new JsonObject(msg.payload().toString()));
        }).publishReleaseHandler(mqttEndpoint::publishComplete);

        mqttEndpoint.pingHandler(msg ->{
            log.info("Receive heatbeat with client id {}", mqttEndpoint.clientIdentifier());
        });
    }

    @Override
    public void stop() {
        mqttServer.close(ar -> {
            if (ar.succeeded()) {
                log.info("Mqtt server closed");
            } else {
                log.info("Mqtt server failed to close");
                ar.cause().printStackTrace();
            }
        });
    }
}
