package shop;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;

import com.google.protobuf.InvalidProtocolBufferException;

import cluster.management.ServiceRegistry;
import model.TaskResponse;
import model.StatusRequest;
import model.StatusResponse;
import model.SerializationUtils;
import model.TaskRequest;
import model.proto.ShopModel;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopCoordinator implements OnRequestCallback {
    private static final String ENDPOINT = "/shop";
    private static final String STATUS_ENDPOINT = "/info";
    private final ServiceRegistry workersServiceRegistry;
    private final WebClient client;
    private final Random random;
    private int prevWorker;
    
    public ShopCoordinator(ServiceRegistry workersServiceRegistry, WebClient client) {
        this.workersServiceRegistry = workersServiceRegistry;
        this.client = client;
        this.random = new Random();
        this.prevWorker = 0;
    }

    public byte[] handleTaskRequest(byte[] requestPayload) {
        try {
            ShopModel.Request request = ShopModel.Request.parseFrom(requestPayload);
            ShopModel.Response response = createResponse(request);

            return response.toByteArray();
        } catch (InvalidProtocolBufferException | KeeperException | InterruptedException e) {
            e.printStackTrace();
            return ShopModel.Response.getDefaultInstance().toByteArray();
        }
    }
    
    private ShopModel.Response createResponse(ShopModel.Request request) throws KeeperException, InterruptedException {
        ShopModel.Response.Builder response = ShopModel.Response.newBuilder();

        System.out.println(String.format("Coordinator received order for %d items", request.getPurchaseQuery()));

        String worker = this.selectWorker();

        if (worker == null) {
            System.out.println("No search workers currently available");
            return response.build();
        }
    	
        long purchaseQuery = request.getPurchaseQuery();
        TaskRequest task = createTask(purchaseQuery);
        TaskResponse results = sendTaskToWorker(worker, task);
        
        response
        	.setItemsPurchased(results.getItemsPurchased())
        	.setItemsAvailable(results.getItemsAvailable());

        return response.build();
    }

    private String selectWorker() throws KeeperException, InterruptedException {
        List<String> workers = workersServiceRegistry.getAllServiceAddresses();

        if (workers.isEmpty()) {
            return null;
        }
        
    	int randomIndex = random.nextInt(workers.size());
    	this.updatePrevWorker(workers.size());
        String worker = workers.get(prevWorker);
        return worker;
    }
    
    private void updatePrevWorker(int workersCount) {
    	this.prevWorker = this.prevWorker + 1 < workersCount ? this.prevWorker + 1 : 0; 
    }
    
    private String checkWorkerStatus(String worker) {
    	CompletableFuture<StatusResponse> infoFuture = new CompletableFuture<>();
    	
    	StatusRequest info = new StatusRequest("ruok");
        byte[] infoPayload = SerializationUtils.serialize(info);
        
        infoFuture = client.requestInfo(worker + "/info", infoPayload);
        
        StatusResponse result = new StatusResponse();
        try {
            result = infoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
        	System.out.println(e.getMessage());
        }

        System.out.println(result.getStatus());
        return result.getStatus();
    }
    
    private TaskResponse sendTaskToWorker(String worker, TaskRequest task) {
        CompletableFuture<TaskResponse> taskFuture = new CompletableFuture<>();

        byte[] payload = SerializationUtils.serialize(task);
        taskFuture = client.sendTask(worker, payload);

        TaskResponse result = new TaskResponse();
        try {
            result = taskFuture.get();
        } catch (InterruptedException | ExecutionException e) {
        }

        return result;
    }

    public TaskRequest createTask(long purchasedItems) {
    	return new TaskRequest(purchasedItems);
    }
    
    public byte[] handleInfoRequest(byte[] requestPayload) {
    	StatusRequest info = (StatusRequest) SerializationUtils.deserialize(requestPayload);
        StatusResponse result = new StatusResponse();
        if (info.getStatus().equals("ruok")) {
        	result.setStatus("imok");
        }
        return SerializationUtils.serialize(result);
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
