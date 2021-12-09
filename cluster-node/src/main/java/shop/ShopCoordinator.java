package shop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;

import com.google.protobuf.InvalidProtocolBufferException;

import cluster.management.ServiceRegistry;
import model.SerializationUtils;
import model.TaskRequest;
import model.TaskResponse;
import model.proto.ShopModel;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopCoordinator implements OnRequestCallback {
    private static final String ENDPOINT = "/shop";
    private final ServiceRegistry workersServiceRegistry;
    private final WebClient client;
    private int prevWorker;
    
    public ShopCoordinator(ServiceRegistry workersServiceRegistry, WebClient client) {
        this.workersServiceRegistry = workersServiceRegistry;
        this.client = client;
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
        
        System.out.println("Coordinator dispatching order to worker");
        TaskResponse results = sendTaskToWorker(worker, task);
        
        response
        	.setItemsPurchased(results.getItemsPurchased())
        	.setItemsAvailable(results.getItemsAvailable());

        return response.build();
    }

    public TaskRequest createTask(long purchasedItems) {
    	return new TaskRequest(purchasedItems);
    }
    
    private String selectWorker() throws KeeperException, InterruptedException {
        List<String> workers = workersServiceRegistry.getAllServiceAddresses();

        if (workers.isEmpty()) {
            return null;
        }
        
        String worker = workers.get(prevWorker);
    	this.updatePrevWorker(workers.size());
        return worker;
    }
    
    private void updatePrevWorker(int workersCount) {
    	this.prevWorker = this.prevWorker + 1 < workersCount ? this.prevWorker + 1 : 0; 
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

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
    
}
