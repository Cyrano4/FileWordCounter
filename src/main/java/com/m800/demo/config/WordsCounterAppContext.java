package com.m800.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WordsCounterAppContext {
	
	@Value("${filescanner.source.location}")
	private String sourceFolderLocation;

	public String getSourceFolderLocation() {
		return sourceFolderLocation;
	}

}
