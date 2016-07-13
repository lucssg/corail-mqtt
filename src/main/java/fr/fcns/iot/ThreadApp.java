package fr.fcns.iot;

import fr.fcns.iot.publisher.MQTTThreadProducer;
import fr.fcns.iot.service.impl.MessageGeneratorImpl;
import fr.fcns.iot.service.impl.PropertiesServiceImpl;
import fr.fcns.iot.factory.IOTFactory;
import fr.fcns.iot.factory.IOTThreadFactory;
import fr.fcns.iot.service.PropertiesService;
import org.fusesource.mqtt.client.MQTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class ThreadApp
{

    private static final Logger LOG = LoggerFactory.getLogger(ThreadApp.class);

    public static final int NB_THREADS = 5;
    public static final String THREAD_PREFIX = "MQTT_Thread_";

    private static void init() throws IOException {
        IOTFactory.getInstance().addComponent(IOTFactory.PROPERTIES_SERVICE, new PropertiesServiceImpl());
    }

    private static void init(String name) throws IOException, URISyntaxException {
        LOG.debug("Init Factory {}", name);
        IOTThreadFactory.getInstance(name).addComponent(IOTFactory.MESSAGE_GENERATOR, new MessageGeneratorImpl());
        PropertiesService ps = new PropertiesServiceImpl();
        IOTThreadFactory.getInstance(name).addComponent(IOTFactory.PROPERTIES_SERVICE, ps);
        MQTT mqtt = new MQTT();
        mqtt.setHost(ps.getProperty("mqtt.host"), Integer.valueOf(ps.getProperty("mqtt.port")));
        IOTThreadFactory.getInstance(name).addComponent(IOTFactory.MQTT, mqtt);
        IOTThreadFactory.getInstance(name).addComponent(IOTFactory.PUBLISHER, new MQTTThreadProducer(name, Integer.valueOf(ps.getProperty("mqtt.message.nb")), Integer.valueOf(ps.getProperty("mqtt.message.size")), Integer.valueOf(ps.getProperty("mqtt.thread.repeat"))));
    }

    public static void main( String[] args ) throws Exception {
        LOG.debug( "Start MQTT Thread POC !!" );
        init();
        int nbThreads = Integer.valueOf(((PropertiesService) IOTFactory.getInstance().getComponent(IOTFactory.PROPERTIES_SERVICE)).getProperty("mqtt.thread.nb"));
        int i = 0;
        while(i < nbThreads) {
            String threadName = THREAD_PREFIX + i;
            LOG.debug("Init Thread {}", threadName);
            init(threadName);

            MQTTThreadProducer producer = (MQTTThreadProducer) IOTThreadFactory.getInstance(threadName).getComponent(IOTFactory.PUBLISHER);
            producer.start();
            ++i;
            Thread.sleep(MQTTThreadProducer.SLEEPING_100);
        }

    }
}
