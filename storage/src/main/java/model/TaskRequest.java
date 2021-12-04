package model;

import java.io.Serializable;

public class TaskRequest implements Serializable {
    private long itemsPurchased;

    public TaskRequest(long itemsPurchased) {
            this.itemsPurchased = itemsPurchased;
    }

    public long getItemsPurchased() {
            return itemsPurchased;
    }
}
