package com.dissys.bananashop.model;

public class Order {
	private long itemsPurchased;
    private long itemsAvailable;
    
	public long getItemsPurchased() {
		return itemsPurchased;
	}
	
	public void setItemsPurchased(long itemsPurchased) {
		this.itemsPurchased = itemsPurchased;
	}
	public long getItemsAvailable() {
		return itemsAvailable;
	}
	public void setItemsAvailable(long itemsAvailable) {
		this.itemsAvailable = itemsAvailable;
	}
		
}
