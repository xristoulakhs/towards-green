package com.aueb.towardsgreen;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = 46L;

	private String requestType;
	private String content;

	public Request(String requestType, String content) {
		this.requestType = requestType;
		this.content = content;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getContent() {
		return content;
	}
}
