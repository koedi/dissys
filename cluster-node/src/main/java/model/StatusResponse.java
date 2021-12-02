package model;

import java.io.Serializable;

public class StatusResponse implements Serializable {
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
