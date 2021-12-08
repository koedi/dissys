package shop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import lock.LockListener;
import lock.WriteLock;
import model.SerializationUtils;
import model.StatusRequest;
import model.StatusResponse;
import model.TaskRequest;
import model.TaskResponse;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopWorker implements OnRequestCallback {
    private static final String ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/task/info";
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
    
    public byte[] handleInfoRequest(byte[] requestPayload) {
    	StatusRequest info = (StatusRequest) SerializationUtils.deserialize(requestPayload);
        StatusResponse result = createInfoResult(info);
        return SerializationUtils.serialize(result);
    }

    private class LockCallback implements LockListener {

    	private TaskRequest task;
    	private TaskResponse result;
    	private WriteLock lockNode;
    	
    	public LockCallback(TaskRequest task, WriteLock lockNode) {
    		this.task = task;
    		this.lockNode = lockNode;
    		this.result = new TaskResponse();
    	}
    	
    	public void lockAcquired() {
    		System.out.println("Node acquired lock");
        
    		CompletableFuture<TaskResponse> future = new CompletableFuture<>();
            byte[] payload = SerializationUtils.serialize(task);
            future = client.sendTask(DB_URL, payload);

			try {
			  result = future.get();
			  } catch (InterruptedException | ExecutionException e) {
				System.out.println(e.getMessage());  
			  }
            	
            System.out.println("************************");
            System.out.println(result.getItemsAvailable());
            System.out.println(result.getItemsPurchased());
            
            try {
        		List<String> a = zooKeeper.getChildren("/_locknode_", false);
        		System.out.println("children a");
        		System.out.println(a.toString());
        	} catch (InterruptedException e) {
        		System.out.println(e.getMessage());
        	} catch (KeeperException e) {
        		System.out.println(e.getMessage());
        	}
            
            this.lockNode.unlock();
    	}
    	
    	public TaskResponse getResponse() {
    		return this.result;
    	}

    	public void lockReleased() {
    		System.out.println("Released lock");
    		try {
        		List<String> b = zooKeeper.getChildren("/_locknode_", false);
        		System.out.println("children b");
        		System.out.println(b.toString());
        	} catch (InterruptedException e) {
        		System.out.println("Error:");
        		System.out.println(e.getMessage());
        	} catch (KeeperException e) {
        		System.out.println("Error:");
        		System.out.println(e.getMessage());
        	} catch (Exception e) {
        		System.out.println("Error:");
        		System.out.println(e.getMessage());
        	}
    	}	
    }
    
    private TaskResponse sendTaskToStorage(TaskRequest task) {
    	System.out.println(String.format("Worker received order for %d items", task.getItemsPurchased()));
    
    	
    	WriteLock lockNode = new WriteLock(this.zooKeeper, "/_locknode_", null);
    	
    	LockCallback cb = new LockCallback(task, lockNode);
    	lockNode.setLockListener(cb);
    	
    	try {
    		lockNode.lock();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}

//    	TaskResponse result = new TaskResponse();
//    	CompletableFuture<TaskResponse> future = new CompletableFuture<>();
    	
    	// result = cb.getResponse();
    	
//    	if (leader.isOwner()) {
//    		System.out.println("Node acquired lock");
//            CompletableFuture<TaskResponse> storageFuture = new CompletableFuture<>();
//
//            byte[] payload = SerializationUtils.serialize(task);
//            storageFuture = client.sendTask(DB_URL, payload);
//
//            try {
//                result = storageFuture.get();
//                
//            } catch (InterruptedException | ExecutionException e) {
//            }
//    	} else {
//    		
//    	}
    	
//    	lockNode.unlock();
    
        return cb.getResponse();
    }
    
    
    private StatusResponse createInfoResult(StatusRequest info) {
    	
        System.out.println("Received info request");
        
        StatusResponse result = new StatusResponse();
        if (info.getStatus().equals("ruok")) {
        	result.setStatus("imok");	
        }
        
        return result;
    }
    
    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
    
    @Override
    public String getStatusEndpoint() {
        return STATUS_ENDPOINT;
    }
    
}
