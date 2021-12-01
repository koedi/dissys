package model;

import java.io.Serializable;

public class Task implements Serializable {
	private long itemsPurchased;

	public Task(long itemsPurchased) {
		this.itemsPurchased = itemsPurchased;
	}

	public long getItemsPurchased() {
		return itemsPurchased;
	}

   
}
