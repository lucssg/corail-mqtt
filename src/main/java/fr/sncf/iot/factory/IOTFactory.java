package fr.sncf.iot.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class IOTFactory {

    public static final String MESSAGE_GENERATOR = "MessageGenerator";
    public static final String MQTT = "MQTT";
    public static final String PROPERTIES_SERVICE = "PropertiesService";
    public static final String PUBLISHER = "Publisher";

    private static IOTFactory factory;

    private Map<String, Object> components;

    private IOTFactory() {
        components = new HashMap<>();
    }

    public static IOTFactory getInstance() {
        if (null == factory) {
            factory = new IOTFactory();
        }
        return factory;
    }

    public void addComponent(String name, Object component) {
        components.put(name, component);
    }

    public Object getComponent(String name) {
        return components.get(name);
    }

}
