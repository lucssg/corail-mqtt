package fr.fcns.iot.service.impl;

import fr.fcns.iot.service.PropertiesService;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class PropertiesServiceImpl implements PropertiesService {

    private Properties properties = new Properties();

    public PropertiesServiceImpl() throws IOException {
        properties.load(ClassLoader.getSystemResourceAsStream("mqtt.properties"));
    }

    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    public void reload() throws IOException {
        properties.clear();
        properties.load(ClassLoader.getSystemResourceAsStream("mqtt.properties"));
    }
}
