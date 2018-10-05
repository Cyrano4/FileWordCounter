package com.m800.demo.actors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.m800.demo.CommonException;
import com.m800.demo.config.AggregateMessage;
import com.m800.demo.config.ParserMessage;
import com.m800.demo.constants.WordCounterConstants;
import com.m800.demo.constants.WordCounterEventTypes;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileParser extends UntypedActor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileParser.class);

	private ActorRef wordAggregatorActorRef;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ParserMessage) {
			ParserMessage pMessage = (ParserMessage) message;
			processLines(pMessage.getFileContent(), pMessage.getFileName());
		} else {
			unhandled(message);
		}

	}

	public FileParser() {
		wordAggregatorActorRef = getContext().actorOf(Props.create(WordsAggregator.class),
				WordCounterConstants.WORDS_AGGREGATOR);
	}

	private void processLines(List<String> fileContent, String filename) throws CommonException {
		if (wordAggregatorActorRef == null) {
			throw new CommonException("Parser Not Register to Scanner");
		}
		LOGGER.info("Sending {} event", WordCounterEventTypes.STARTOFFILE);
		wordAggregatorActorRef.tell(new AggregateMessage(WordCounterEventTypes.STARTOFFILE.getValue(), null, filename),
				getSelf());
		for (String fileLine : fileContent) {
			LOGGER.info("Sending {} events", WordCounterEventTypes.LINE);
			wordAggregatorActorRef.tell(new AggregateMessage(WordCounterEventTypes.LINE.getValue(), fileLine, filename),
					getSelf());
		}
		LOGGER.info("Sending {} event", WordCounterEventTypes.ENDOFFILE);
		wordAggregatorActorRef.tell(new AggregateMessage(WordCounterEventTypes.ENDOFFILE.getValue(), null, filename),
				getSelf());
	}

}
