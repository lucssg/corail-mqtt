package fr.sncf.iot.publisher;

import fr.sncf.iot.factory.IOTFactory;
import fr.sncf.iot.factory.IOTThreadFactory;
import fr.sncf.iot.service.MessageGenerator;
import fr.sncf.iot.service.PropertiesService;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class MQTTThreadProducer extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(MQTTThreadProducer.class);
    public static final int SLEEPING = 5000;
    public static final int SLEEPING_100 = 100;
    public static final int REPEAT_100 = 100;

    private MessageGenerator generator;

    private PropertiesService propertiesService;

    private MQTT mqtt;

    private FutureConnection connection;

    private int nbMessages;
    private int size;
    private int repeat = 1;

    public MQTTThreadProducer(String name, int nbMessages, int size) {
        super(name);
        this.nbMessages = nbMessages;
        this.size = size;
        generator = (MessageGenerator) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.MESSAGE_GENERATOR);
        propertiesService = (PropertiesService) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.PROPERTIES_SERVICE);
        mqtt = (MQTT) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.MQTT);
        connection = mqtt.futureConnection();
    }

    public MQTTThreadProducer(String name, int nbMessages, int size, int repeat) {
        super(name);
        this.nbMessages = nbMessages;
        this.size = size;
        this.repeat = repeat;
        generator = (MessageGenerator) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.MESSAGE_GENERATOR);
        propertiesService = (PropertiesService) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.PROPERTIES_SERVICE);
        mqtt = (MQTT) IOTThreadFactory.getInstance(name).getComponent(IOTFactory.MQTT);
        connection = mqtt.futureConnection();
    }

    @Override
    public synchronized void start() {
        super.start();
        connect();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        LOG.debug("Thread {} - repeat: {}", getName(), repeat);
        LOG.debug("Masse Message START (" + nbMessages * repeat + ")");
        LOG.debug("Retain: " + Boolean.parseBoolean(propertiesService.getProperty("mqtt.topic.retain")));
        super.run();
        int loop = 0;
        while (repeat > loop) {
            try {
                massPublish(nbMessages, size);
                Thread.sleep(SLEEPING);
            } catch (InterruptedException e) {
                // do nothing
            } catch (Exception e) {
                LOG.error("Thread KO!!", e);
            }
            ++loop;
        }
        LOG.debug("Masse Message STOP (" + nbMessages + ")");
    }

    public Future<Void> connect() {
        LOG.debug("Connexion MQTT");
        Future<Void> f1 = connection.connect();
        try {
            f1.await();
        } catch (Exception e) {
            LOG.error("Connection error", e);
            throw new RuntimeException(e);
        }
        return f1;
    }

    public Future<Void> disconnect() {
        LOG.debug("Déconnexion MQTT");
        Future<Void> f1 = connection.disconnect();
        try {
            f1.await();
        } catch (Exception e) {
            LOG.error("Disconnect error", e);
            throw new RuntimeException(e);
        }
        return f1;
    }


    public void massPublish(int nbMessages, int size) throws Exception {
        for (int i = 0; i < nbMessages; ++i) {
            byte[] message = generator.generate(size).getBytes();
            Future<Void> publish = connection.publish(propertiesService.getProperty("mqtt.topic.publish"), message, QoS.AT_LEAST_ONCE, Boolean.parseBoolean(propertiesService.getProperty("mqtt.topic.retain")));
            publish.await();
        }
    }
}
