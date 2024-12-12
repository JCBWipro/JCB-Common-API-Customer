package com.wipro.jcb.livelink.app.dataprocess.kafkaconsumer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wipro.jcb.livelink.app.dataprocess.dataparser.CombinedHistoryDataParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

@Slf4j
@Component
public class CombinedHistoryConsumer implements Runnable {

    @Value("${CombinedHistoryThreadCount}")
    int numberOfThreads;

    @Value("${kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${kafka.topic-name}")
    String topicName;

    @Value("${kafka.enable-auto-commit}")
    boolean enableAutoCommit;

    @Value("${kafka.key-deserializer}")
    String keyDeserializer;

    @Value("${kafka.value-deserializer}")
    String valueDeserializer;

    private volatile boolean running = true;// Use a volatile flag for shutdown
    private KafkaConsumer<String, String> consumer;
    private ExecutorService executor;


    @PostConstruct  // Initialize after bean creation
    public void init() {
        // Configure Kafka consumer properties
        String groupId = "Client_" + topicName;
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id",groupId);
        props.put("enable.auto.commit", enableAutoCommit);
        props.put("key.deserializer", keyDeserializer);
        props.put("value.deserializer", valueDeserializer);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName)); // Subscribe to the topic

        // Create thread pool
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("CombinedHistoryConsumer-thread-%d").build();
        executor = Executors.newFixedThreadPool(numberOfThreads, namedThreadFactory);

        // Start the consumer thread
        new Thread(this, "CombinedHistoryConsumer").start();
    }

    @Override
    public void run() {
        log.info("CombinedHistoryConsumer started.  topicName={}, numberOfThreads={}", topicName, numberOfThreads); // Startup message

        while (running) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    log.debug("Received {} records from Kafka", records.count());  // Log number of records received
                    List<Future<?>> futures = new ArrayList<>();

                    for (ConsumerRecord<String, String> record : records) {
                        futures.add(executor.submit(() -> {
                            try {
                                String msgReceived = record.value();
                                log.trace("Processing message: {}", msgReceived);
                                CombinedHistoryDataParser.dataParsing(msgReceived);
                            } catch (Exception e) {
                                log.error("Error processing message: {} - Exception: {}", record.value(), e.getMessage(), e);
                            }
                        }));
                    }

                    for (Future<?> future : futures) {
                        try {
                            future.get();  // Get the result of each task.  This blocks and will re-throw any exceptions
                        } catch (InterruptedException e) {
                            log.warn("Task interrupted: {}", e.getMessage());
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            log.error("Task execution failed: {}", e.getMessage(), e.getCause()); // Log the causing exception
                        }
                    }
                } else {
                    log.trace("No records received from Kafka. Polling again..."); // Trace-level logging to avoid excessive output
                }

            } catch (Exception e) {
                log.error("Unexpected error in consumer loop: {}", e.getMessage(), e); // Catch and log any other exception
            }
        }
        shutdown();
    }

    public void shutdown() {
        running = false; // Signal the consumer loop to stop
        if (executor != null) {
            executor.shutdown(); // Initiate shutdown of the executor
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { // Wait for termination
                    executor.shutdownNow(); // Forceful shutdown if tasks don't complete in time
                }
            } catch (InterruptedException e) {
                log.error("Interrupted while waiting for executor shutdown", e);
                Thread.currentThread().interrupt();
            }
        }

        if (consumer != null) {
            consumer.close();
        }
        log.info("CombinedHistoryConsumer shutdown complete");
    }
}

