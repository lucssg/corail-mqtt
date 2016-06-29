package fr.sncf.iot;

import fr.sncf.iot.factory.IOTFactory;
import fr.sncf.iot.publisher.MQTTPublisher;
import fr.sncf.iot.service.PropertiesService;
import fr.sncf.iot.service.impl.MessageGeneratorImpl;
import fr.sncf.iot.service.impl.PropertiesServiceImpl;
import org.fusesource.mqtt.client.MQTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static final int NB_MESSAGES_1000 = 1000;
    private static final int NB_MESSAGES_100000 = 100000;
    private static final int NB_MESSAGES_10000 = 10000;
    private static final int NB_MESSAGES_10 = 10;

    private static void init() throws IOException, URISyntaxException {
        LOG.debug("Init Factory");
        IOTFactory.getInstance().addComponent(IOTFactory.MESSAGE_GENERATOR, new MessageGeneratorImpl());
        PropertiesService ps = new PropertiesServiceImpl();
        IOTFactory.getInstance().addComponent(IOTFactory.PROPERTIES_SERVICE, ps);
        MQTT mqtt = new MQTT();
        mqtt.setHost(ps.getProperty("mqtt.host"), Integer.valueOf(ps.getProperty("mqtt.port")));
        IOTFactory.getInstance().addComponent(IOTFactory.MQTT, mqtt);
        IOTFactory.getInstance().addComponent(IOTFactory.PUBLISHER, new MQTTPublisher());
    }

    public static void main( String[] args ) throws Exception {
        LOG.debug( "Start MQTT POC !!" );
        init();

        MQTTPublisher publisher = (MQTTPublisher) IOTFactory.getInstance().getComponent(IOTFactory.PUBLISHER);
        publisher.connect();

        publisher.massPublish(NB_MESSAGES_1000);

        publisher.disconnect();

    }
}
