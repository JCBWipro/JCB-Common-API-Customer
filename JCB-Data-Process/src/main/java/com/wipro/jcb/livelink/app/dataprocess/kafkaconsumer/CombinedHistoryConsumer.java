/*
package com.wipro.jcb.livelink.app.dataprocess.kafkaconsumer;


import com.wipro.jcb.livelink.app.dataprocess.dataparser.CombinedHistoryDataParser;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

*/
/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 *//*

public class CombinedHistoryConsumer implements Runnable {

    Thread t;
    String topicName = "CombinedHistory";
    static int numberOfThreads = 0;
    static int listenerFlag = 1;
    public static KafkaConsumer<String, String> consumer = null;//CR330.n
    public static ExecutorService executor;;//CR330.n

    public CombinedHistoryConsumer() {
        t = new Thread(this, "CombinedHistoryConsumer");
        t.start();
    }

    //CR330.sn
    @Override
    public void run() {
        Logger iLogger = InfoLoggerClass.logger;
        Logger fLogger = FatalLoggerClass.logger;

        //--------- Step1: Get the number of threads that can run in parallel after consuming the message from Kafka
        try {
            Properties prop = new StaticProperties().getConfProperty();
            numberOfThreads = Integer.parseInt(prop.getProperty("CombinedHistoryThreadCount"));
        } catch (Exception e) {
            fLogger.fatal("CombinedHistoryConsumer:Error in intializing property File:" + e.getMessage());
        }

        //Need to stop the thread if there is an error in reading from the property file
        if (numberOfThreads != 0) {
            try {
                //------------ Step2: Provide the connection parameters
                String groupId = "Client_" + topicName;
                Properties props = new Properties();
                props.put("bootstrap.servers", "localhost:9092");
                props.put("group.id", groupId);
                props.put("enable.auto.commit", "true");
                props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

                ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                        .setNameFormat("CombinedHistoryConsumer-thread-%d").build();
                List<TopicPartition> partitionList = new LinkedList<TopicPartition>();
                TopicPartition partition = new TopicPartition(topicName, 0);
                partitionList.add(partition);
                CombinedHistoryConsumer.consumer = new KafkaConsumer<>(props);
                CombinedHistoryConsumer.consumer.assign(partitionList);

                outerLoop: while (numberOfThreads != 0) {
                    int msgsProcessed = 0;
                    int ActiveCount = 0;

                    if (CombinedHistoryConsumer.listenerFlag == 1) {
                        ConsumerRecords<String, String> streams = CombinedHistoryConsumer.consumer.poll(100);

                        if (streams != null && !streams.isEmpty()) {
                            //We are using Fixed Pool Executor and given numberOfThreads as fixed and not the number of messages in Kafka,
                            //since there is no method available to find the number of messages to be consumed yet from Kafka.
                            //Kafka Topic is considered to be of infinite messages.

                            executor = Executors.newFixedThreadPool(numberOfThreads, namedThreadFactory);

                            //Important Note: Kafka stream will not come out of this for loop even after consuming all the messages.
                            //It would be waiting for the next set of messages to be consumed and hence it acts like a continuous listener as long as this thread exists.
                            for (ConsumerRecord<String, String> stream : streams) {
                                if (CombinedHistoryConsumer.listenerFlag == 0) {
                                    int infiniteCounter = 0;
                                    iLogger.info(
                                            "CombinedHistoryConsumer:ForKafkaStreamStart:Listener Flag is set to 0. Hence stop consuming messages from Topic");
                                    executor.shutdown();
                                    while (!executor.isTerminated()) {
                                        if (infiniteCounter == 0) {
                                            iLogger.info(
                                                    "CombinedHistoryConsumer:ForKafkaStreamStart:Waiting for Termination");
                                            infiniteCounter++;
                                        }
                                        Thread.sleep(100);
                                    }
                                    executor.shutdownNow();
                                    infiniteCounter = 0;
                                    while (CombinedHistoryConsumer.listenerFlag == 0) {
                                        if (infiniteCounter == 0) {
                                            iLogger.info(
                                                    "CombinedHistoryConsumer:ForKafkaStreamStart:Listener Flag ==0 ");
                                            infiniteCounter++;
                                        }
                                        Thread.sleep(1000);
                                    }
                                    iLogger.info(
                                            "CombinedHistoryConsumer:Control Came out of Infinite Loop when Lister Falg Set to 1");
                                    continue outerLoop;
                                }

                                if (executor instanceof ThreadPoolExecutor) {
                                    Thread.sleep(10);

                                    ActiveCount = ((ThreadPoolExecutor) executor).getActiveCount();
                                    iLogger.debug("MDA:DI:MDAConsumer: ActiveCount :" + ActiveCount);
                                    activeCountCheck: while (ActiveCount >= numberOfThreads) {
                                        Thread.sleep(500);
                                        ActiveCount = ((ThreadPoolExecutor) executor).getActiveCount();
                                        if (CombinedHistoryConsumer.listenerFlag == 0) {
                                            break activeCountCheck;
                                        }
                                    }
                                }

                                if (CombinedHistoryConsumer.listenerFlag == 0) {
                                    int infiniteCounter = 0;
                                    iLogger.info(
                                            "CombinedHistoryConsumer:It.hasNext():Listener Flag is set to 0. Hence stop consuming messages from Topic");
                                    executor.shutdown();
                                    while (!executor.isTerminated()) {
                                        if (infiniteCounter == 0) {
                                            iLogger.info("CombinedHistoryConsumer:It.hasNext():Waiting for Termination");
                                            infiniteCounter++;
                                        }
                                        Thread.sleep(100);
                                    }
                                    executor.shutdownNow();

                                    infiniteCounter = 0;
                                    while (CombinedHistoryConsumer.listenerFlag == 0) {
                                        if (infiniteCounter == 0) {
                                            iLogger.info("CombinedHistoryConsumer:It.hasNext():Listener Flag ==0 ");
                                            infiniteCounter++;
                                        }
                                        Thread.sleep(1000);
                                    }
                                    iLogger.info(
                                            "CombinedHistoryConsumer:Control Came out of Infinite Loop when Lister Falg Set to 1");
                                    continue outerLoop;
                                }
                                msgsProcessed++;
                                iLogger.debug(
                                        "CombinedHistoryConsumer:Number of Messages processed  =" + msgsProcessed);

                                //the msgReceived variable is consisting of the actual values (packets) that we need to persist into the DB,
                                //we need to parse the data, process the data to split it and insert it into respective tables .
                                String msgReceived = stream.value();
                                iLogger.info("CombinedHistoryConsumer:ActiveCount : " + ActiveCount + " msgReceived ="
                                        + msgReceived);

                                */
/*StreamRabbitMQConsumer streamConsumer = new StreamRabbitMQConsumer();
                                streamConsumer.payload = msgReceived;
                                executor.execute(streamConsumer);*//*

                            }
                            CombinedHistoryDataParser.dataParsing(msgReceived);
                            executor.shutdown();

                            //--------- Introducing while loop here to wait for all the threads to be completed, so that next set of packets can be consumed from Kafka
                            //Otherwise it would just bombard the executor where in executor can't open up new threads until it is released from other
                            iLogger.debug("CombinedHistoryConsumer:Wait for Executor to terminate");
                            int infiniteCounter1 = 0;
                            while (!executor.isTerminated()) {
                                if (infiniteCounter1 == 0) {
                                    iLogger.info(
                                            "CombinedHistoryConsumer:It.When For loops Ended :Waiting for Termination");
                                    infiniteCounter1++;
                                }
                            }
                            executor.shutdownNow();
                            iLogger.debug("CombinedHistoryConsumer:Executor terminated");
                        }
                    }else {
                        fLogger.debug(
                                "CombinedHistoryConsumer:Listener Flag is set to 0. Hence not consuming messages from Topic");
                        break outerLoop;
                    }
                }
            }catch (Exception e) {
                fLogger.fatal("CombinedHistoryConsumer:Unable to subscribe kafka consumer:Exception:" + e.getMessage());
            }finally {
                CombinedHistoryConsumer.consumer.close();
                iLogger.info("CombinedHistoryConsumer:End on Thread Run ");
            }
        }
    }

}
*/
