package shop;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.ZooKeeper;

import lock.LockListener;
import lock.WriteLock;
import model.SerializationUtils;
import model.TaskRequest;
import model.TaskResponse;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopWorker implements OnRequestCallback {
    private static final String ENDPOINT = "/task";
    private static final String DB_URL = "http://localhost:9001/orders";
    private final WebClient client;
    private ZooKeeper zooKeeper;

    public ShopWorker(WebClient client, ZooKeeper zooKeeper) {    
        this.client = client;
        this.zooKeeper = zooKeeper;
    }
    
    public byte[] handleTaskRequest(byte[] requestPayload) {
        TaskRequest task = (TaskRequest) SerializationUtils.deserialize(requestPayload);        
        TaskResponse result = sendTaskToStorage(task);
        return SerializationUtils.serialize(result);
    }
   
    private TaskResponse sendTaskToStorage(TaskRequest task) {
    	System.out.println(String.format("Worker received order for %d items", task.getItemsPurchased()));
    	
    	LockCallback cb = this.tryToAcquireLock(task);

    	CompletableFuture<TaskResponse> future = new CompletableFuture<>();
    	future = cb.getResponse();
    	
    	TaskResponse result = new TaskResponse();
    	try {
    		result = future.get();
    	} catch (InterruptedException | ExecutionException e) {
    		System.out.println(e.getMessage());
    	}
    
        return result;
    }
    
    private LockCallback tryToAcquireLock(TaskRequest task) {
    	WriteLock lockNode = new WriteLock(this.zooKeeper, "/_locknode_", null);    	
    	LockCallback cb = new LockCallback(task, lockNode);
    	lockNode.setLockListener(cb);
    	
    	try {
    		lockNode.lock();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	
    	return cb;
    }
    
    private class LockCallback implements LockListener {

    	private TaskRequest task;
    	CompletableFuture<TaskResponse> future;
    	private WriteLock lockNode;
    	
    	public LockCallback(TaskRequest task, WriteLock lockNode) {
    		this.task = task;
    		this.lockNode = lockNode;
    		this.future = new CompletableFuture<>();
    	}
    	
    	public void lockAcquired() {
    		System.out.println("Node acquired lock");
        
            byte[] payload = SerializationUtils.serialize(task);
            future = client.sendTask(DB_URL, payload);

            this.lockNode.unlock();
    	}
    	
    	public CompletableFuture<TaskResponse> getResponse() {
    		return this.future;
    	}

    	public void lockReleased() {
    		System.out.println("Node released lock");
    	}	
    }
    
    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
   
}
