package com.m800.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.m800.demo.actors.FileScanner;
import com.m800.demo.config.WordsCounterAppContext;
import com.m800.demo.constants.WordCounterConstants;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;



/**
 * 
 * @author vkanuri
 * 
 */
@Component
@Profile("!test")
public class FileWordCounterApplicationRunner implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWordCounterApplicationRunner.class);

	@Autowired
	private ConfigurableApplicationContext appContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			LOGGER.debug("****Starting Application Running****");
			FileWordCounterApplicationRunner akkaDriver = appContext.getBean(FileWordCounterApplicationRunner.class);
			akkaDriver.run(appContext.getBean(WordsCounterAppContext.class));
			LOGGER.debug("****Completed Application Running****");
		} catch (Exception e) {
			LOGGER.error("There was a problem running the application", e);
		} finally {
			System.exit(SpringApplication.exit(appContext));
		}

	}

	public void run(WordsCounterAppContext ctx) {
		try {
			ActorSystem system = ActorSystem.create(WordCounterConstants.APP_NAME);
			ActorRef fileScanner = system.actorOf(Props.create(FileScanner.class, ctx.getSourceFolderLocation()),
					WordCounterConstants.FILE_SCANER);
			fileScanner.tell("tick", fileScanner);
			system.shutdown();
		} catch (Exception ce) {
			LOGGER.error("Exception : {}", ce.getMessage(), ce);
		}
	}
}
