package com.m800.demo.actors;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.m800.demo.CommonException;
import com.m800.demo.config.ParserMessage;
import com.m800.demo.constants.WordCounterConstants;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class FileScanner extends UntypedActor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileScanner.class);

	private ActorRef fileParserActorRef;

	private String sourceFolder;

	public FileScanner(String sourceFolder) {
		this.sourceFolder = sourceFolder;
		fileParserActorRef = getContext().actorOf(Props.create(FileParser.class), WordCounterConstants.FILE_PARSER);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		List<String> sourceFiles = getFiles();
		if (sourceFiles != null) {
			LOGGER.info("No of Files Found to Process : {}", sourceFiles.size());
		}
		for (String fileName : sourceFiles) {
			List<String> fileContent = getFileLines(fileName);
			sendToParser(fileContent, fileName);
		}
	}

	private List<String> getFiles() throws IOException, URISyntaxException, CommonException {
		LOGGER.info("Source Directory: {}", sourceFolder);
		if (StringUtils.isEmpty(sourceFolder)) {
			throw new CommonException("Invalid Source folder");
		}
		try {
			File file = new File(sourceFolder);

			if (file != null && !file.isDirectory()) {
				throw new CommonException("Invalid Source folder");
			}
			File[] filesList = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".txt")||filename.endsWith(".rtf");
				}
			});
			return Arrays.asList(filesList).stream().map(File::getName).collect(Collectors.toList());
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			throw new CommonException(e);
		}

	}

	private List<String> getFileLines(String filename) throws CommonException {
		try {
			LOGGER.info("Source File: " + filename);
			String inputFilePath = sourceFolder + File.separator + filename;
			Path path = Paths.get(inputFilePath);
			return Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (Exception e) {
			throw new CommonException("IOException While Reading File : {}" + filename, e);
		}

	}

	private void sendToParser(List<String> fileContent, String filename) throws CommonException {
		if (fileParserActorRef == null) {
			throw new CommonException("Parser Not Register to Scanner");
		}
		if (fileContent != null && !fileContent.isEmpty()) {
			fileParserActorRef.tell(new ParserMessage(filename, fileContent), getSelf());
			LOGGER.info(
					String.format("FileScanner: Delegates to Parser to process file Content of File : %s", filename));
		} else {
			LOGGER.info(String.format("FileScanner: No File Content to process in File : %s", filename));
		}
	}

}