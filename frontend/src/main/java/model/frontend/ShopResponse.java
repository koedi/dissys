package model.frontend;

public class ShopResponse {
	private ResultInfo results; 
    
    public ShopResponse(ResultInfo results) {
		this.results = results;
	}

    public ResultInfo getResults() {
		return results;
	}

	public static class ResultInfo {
        private long purchasedItems;
        private long availableItems;
        
		public ResultInfo(long purchasedItems, long availableItems) {
			this.purchasedItems = purchasedItems;
			this.availableItems = availableItems;
		}

		public long getPurchasedItems() {
			return purchasedItems;
		}

		public void setPurchasedItems(long purchasedItems) {
			this.purchasedItems = purchasedItems;
		}

		public long getAvailableItems() {
			return availableItems;
		}

		public void setAvailableItems(long availableItems) {
			this.availableItems = availableItems;
		}
        
    }
}
