package com.m800.demo.config;

import java.util.List;

/**
 * Message Container For Sending File Content and FileName to Parser from
 * Scanner
 * 
 * @author vkanuri
 *
 */
public class ParserMessage {

	private String fileName;
	private List<String> fileContent;

	public ParserMessage(String fileName, List<String> fileContent) {
		super();
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getFileContent() {
		return fileContent;
	}

	public void setFileContent(List<String> fileContent) {
		this.fileContent = fileContent;
	}

}
