package com.wipro.jcb.livelink.app.dataprocess.kafkaconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.dataprocess.dataparser.CombinedHistoryDataParser;

@Service
public class MachineDataConsumer {
	
	@Autowired
	CombinedHistoryDataParser combinedHistoryDataParser;
	
	@KafkaListener(topics="machineData", groupId = "Client_machineData")
	public void listenToTopic(String message) {
		System.out.println("The Message received in JCB-Data-Processor-MS for consumption is : " + message);
		combinedHistoryDataParser.dataParsing(message);
	}

}
