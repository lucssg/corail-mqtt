package fr.fcns.iot.service;

import java.io.IOException;

/**
 * Created by lmarchau on 28/06/2016.
 */
public interface PropertiesService {

    String getProperty(String key);

    void reload() throws IOException;

}
