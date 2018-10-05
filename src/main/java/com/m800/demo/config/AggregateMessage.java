package com.m800.demo.config;
/**
 * Message Container For Sending Line Content and Event Type to Aggregator from Parser
 * @author vkanuri
 *
 */
public class AggregateMessage {

	private String eventType;
	private String lineContent;
	private String fileName;

	public AggregateMessage(String eventType, String lineContent, String fileName) {
		super();
		this.eventType = eventType;
		this.lineContent = lineContent;
		this.fileName = fileName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getLineContent() {
		return lineContent;
	}

	public void setLineContent(String lineContent) {
		this.lineContent = lineContent;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
