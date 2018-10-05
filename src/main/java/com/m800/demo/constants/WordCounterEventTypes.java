package com.m800.demo.constants;

public enum WordCounterEventTypes {

	STARTOFFILE("start-of-file"), LINE("line"), ENDOFFILE("end-of-file");
	String value;

	WordCounterEventTypes(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
