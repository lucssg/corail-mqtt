package fr.sncf.iot.service.impl;

import fr.sncf.iot.service.MessageGenerator;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by lmarchau on 28/06/2016.
 */
public class MessageGeneratorImpl implements MessageGenerator {


    private int nb = 0;

    public String generate(int size) {
        ++nb;
        return String.valueOf(nb) + RandomStringUtils.randomAlphabetic(size);
    }

}
