package fr.fcns.iot.service.impl;

import fr.fcns.iot.service.MessageGenerator;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by lmarchau on 12/07/2016.
 */
public class CapteurMessageGeneratorImpl implements MessageGenerator {

    private static final int ID_SIZE = 12;

    private static final Random RANDOM_OPEN_CLOSE = new Random(2);

    private String id;

    public CapteurMessageGeneratorImpl() {
        id =  RandomStringUtils.randomAlphanumeric(ID_SIZE);
    }

    @Override
    public String generate(int size) {
        return id + ZonedDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()) + RANDOM_OPEN_CLOSE.nextBoolean();
    }
}
