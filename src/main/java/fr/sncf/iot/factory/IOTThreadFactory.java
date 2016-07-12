package fr.sncf.iot.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class IOTThreadFactory {

    private static Map<String, IOTThreadFactory> factories = new HashMap<>();

    private Map<String, Object> components;

    private IOTThreadFactory() {
        components = new HashMap<>();
    }

    public static IOTThreadFactory getInstance(String key) {
        if (!factories.containsKey(key)) {
            factories.put(key, new IOTThreadFactory());
        }
        return factories.get(key);
    }

    public void addComponent(String name, Object component) {
        components.put(name, component);
    }

    public Object getComponent(String name) {
        return components.get(name);
    }

}
