package com.wipro.jcb.livelink.app.service.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-05-2024
 * project: msKafka
 */
@Service
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    @KafkaListener(topics = "${message.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message){
        log.info("Consumed message: '{}'", message);
    }
}
