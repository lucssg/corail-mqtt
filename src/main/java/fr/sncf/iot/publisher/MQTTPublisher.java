package fr.sncf.iot.publisher;

import fr.sncf.iot.factory.IOTFactory;
import fr.sncf.iot.service.MessageGenerator;
import fr.sncf.iot.service.PropertiesService;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class MQTTPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(MQTTPublisher.class);

    private MessageGenerator generator;

    private PropertiesService propertiesService;

    private MQTT mqtt;

    FutureConnection connection;

    public MQTTPublisher() {
        generator = (MessageGenerator) IOTFactory.getInstance().getComponent(IOTFactory.MESSAGE_GENERATOR);
        propertiesService = (PropertiesService) IOTFactory.getInstance().getComponent(IOTFactory.PROPERTIES_SERVICE);
        mqtt = (MQTT) IOTFactory.getInstance().getComponent(IOTFactory.MQTT);
        connection = mqtt.futureConnection();
    }

    public Future<Void> connect() throws Exception {
        LOG.debug("Connexion MQTT");
        Future<Void> f1 = connection.connect();
        f1.await();
        return f1;
    }

    public Future<Void> disconnect() throws Exception {
        LOG.debug("Déconnexion MQTT");
        Future<Void> f1 = connection.disconnect();
        f1.await();
        return f1;
    }


    public void massPublish(int nbMessages) throws Exception {
        LOG.debug("Masse Message START (" + nbMessages + ")");
        LOG.debug("Retain: " + Boolean.parseBoolean(propertiesService.getProperty("mqtt.topic.retain")));
        for (int i = 0; i < nbMessages; ++i) {
            byte[] message = generator.generate(MessageGenerator.MSG_SIZE_100).getBytes();
//            LOG.info("Message: {}", new String(message));
            Future<Void> publish = connection.publish(propertiesService.getProperty("mqtt.topic.publish"), message, QoS.AT_LEAST_ONCE, Boolean.parseBoolean(propertiesService.getProperty("mqtt.topic.retain")));
            publish.await();
        }
        LOG.debug("Masse Message STOP (" + nbMessages + ")");
    }
}
