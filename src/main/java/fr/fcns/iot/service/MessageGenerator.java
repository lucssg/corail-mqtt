package fr.fcns.iot.service;

/**
 * Created by lmarchau on 28/06/2016.
 */
public interface MessageGenerator {

    int MSG_SIZE_10 = 10;
    int MSG_SIZE_100 = 100;

    String generate(int size);

}
