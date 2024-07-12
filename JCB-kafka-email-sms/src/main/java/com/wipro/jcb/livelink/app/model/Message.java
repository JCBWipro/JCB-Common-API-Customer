package com.wipro.jcb.livelink.app.model;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:08-07-2024
 * project: msKafka
 */
@Data
public class Message {

    private static final Logger log = LoggerFactory.getLogger(Message.class);
    private String content;
    private List<String> mobileNumber;
    private LocalDateTime timeStamp;


    public Message(String content, List<String> mobileNumber, LocalDateTime timeStamp) {
        this.content = content;
        this.mobileNumber = mobileNumber;
        this.timeStamp = timeStamp;
        log.info("Message parameterized constructor called : {}", this);
    }

    public Message() {

    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
