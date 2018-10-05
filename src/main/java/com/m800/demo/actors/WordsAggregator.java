package com.m800.demo.actors;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.m800.demo.config.AggregateMessage;
import com.m800.demo.constants.WordCounterEventTypes;

import akka.actor.UntypedActor;

public class WordsAggregator extends UntypedActor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WordsAggregator.class);
	private Map<String, Integer> countMap = new HashMap<String, Integer>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof AggregateMessage) {
			AggregateMessage lineData = (AggregateMessage) message;
			if (WordCounterEventTypes.STARTOFFILE.getValue().equalsIgnoreCase(lineData.getEventType())) {
				LOGGER.info("Received STARTOFFILE Event for File : {}",lineData.getFileName());
				countMap.put(lineData.getFileName(), new Integer("0"));
			} else if (WordCounterEventTypes.ENDOFFILE.getValue().equalsIgnoreCase(lineData.getEventType())) {
				LOGGER.info("Received ENDOFFILE Event for File : {}",lineData.getFileName());
				Integer wordCount = countMap.get(lineData.getFileName());
				String resultMessage = String.format("Count of words in the File %s is : %d", lineData.getFileName(),wordCount);
				System.out.println(resultMessage);
				LOGGER.info(resultMessage);
				countMap.remove(lineData.getFileName());
			} else if (WordCounterEventTypes.LINE.getValue().equalsIgnoreCase(lineData.getEventType())) {
				LOGGER.info("Received LINE Event for File : {}",lineData.getFileName());
				countAndAggregate(lineData.getLineContent(), lineData.getFileName());

			}

		} else
			unhandled(message);
	}

	private void countAndAggregate(String lineData, String fileName) {
		Integer count = new Integer("0");
		if (lineData != null && lineData.length() > 0) {
			String[] lineWordsCount = lineData.split(" ");
			if (countMap.containsKey(fileName)) {
				count = countMap.get(fileName);
			}
			count = count + lineWordsCount.length;
			countMap.put(fileName, count);
		}
	}

}
