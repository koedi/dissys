package model;

import java.io.Serializable;

public class StatusRequest implements Serializable {
	private String status;
	
	public StatusRequest(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
}
