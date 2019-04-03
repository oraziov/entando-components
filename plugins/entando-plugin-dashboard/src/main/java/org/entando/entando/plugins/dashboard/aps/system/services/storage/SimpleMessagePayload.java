package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import com.google.gson.Gson;

public class SimpleMessagePayload implements MessagePayload {

	private String value;

	public SimpleMessagePayload() {

	}
	
	public SimpleMessagePayload(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getJson() {
		return new Gson().toJson(this);
	}
	
}
