package shop;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;

import com.google.protobuf.InvalidProtocolBufferException;

import cluster.management.ServiceRegistry;
import model.SerializationUtils;
import model.StatusRequest;
import model.StatusResponse;
import model.TaskRequest;
import model.TaskResponse;
import model.proto.ShopModel;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopWorker implements OnRequestCallback {
    private static final String ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/task/info";
    private static final String DB_URL = "http://localhost:9001/orders";
    private final WebClient client;

    public ShopWorker(WebClient client) {    
        this.client = client;
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

    private TaskResponse sendTaskToStorage(TaskRequest task) {
    	System.out.println(String.format("Worker received order for %d items", task.getItemsPurchased()));
    	
        CompletableFuture<TaskResponse> storageFuture = new CompletableFuture<>();

        byte[] payload = SerializationUtils.serialize(task);
        storageFuture = client.sendTask(DB_URL, payload);

        TaskResponse result = new TaskResponse();
        try {
            result = storageFuture.get();
        } catch (InterruptedException | ExecutionException e) {
        }

        return result;
    }
    
    private TaskResponse createTaskResult(TaskRequest task) {
        System.out.println(String.format("Received order for %d items", task.getItemsPurchased()));
        
        TaskResponse result = new TaskResponse();
        result.setItemsPurchased(task.getItemsPurchased());
        result.setItemsAvailable(100);

        return result;
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
