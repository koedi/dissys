package shop;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.zookeeper.KeeperException;

import com.google.protobuf.InvalidProtocolBufferException;

import cluster.management.ServiceRegistry;
import model.Result;
import model.SerializationUtils;
import model.Task;
import model.proto.ShopModel;
import networking.OnRequestCallback;
import networking.WebClient;

public class ShopCoordinator implements OnRequestCallback {
    private static final String ENDPOINT = "/shop";
    private final ServiceRegistry workersServiceRegistry;
    private final WebClient client;
    private final Random random;
    
    public ShopCoordinator(ServiceRegistry workersServiceRegistry, WebClient client) {
        this.workersServiceRegistry = workersServiceRegistry;
        this.client = client;
        this.random = new Random();
    }

    public byte[] handleRequest(byte[] requestPayload) {
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

        System.out.println("Received purchase query: " + request.getPurchaseQuery());

        long purchaseQuery = request.getPurchaseQuery();
        
        List<String> workers = workersServiceRegistry.getAllServiceAddresses();

        if (workers.isEmpty()) {
            System.out.println("No search workers currently available");
            return response.build();
        }

        Task task = createTask(purchaseQuery);
        Result results = sendTasksToWorkers(workers, task);

        response
        	.setItemsAvailable(results.getItemsAvailable())
        	.setItemsPurchased(results.getItemsPurchased());

        return response.build();
    }

    private Result sendTasksToWorkers(List<String> workers, Task task) {
        CompletableFuture<Result> future = new CompletableFuture<>();
        
        int randomIndex = random.nextInt(workers.size());
        String worker = workers.get(randomIndex);
        
        byte[] payload = SerializationUtils.serialize(task);
        future = client.sendTask(worker, payload);

        Result result = new Result();
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
        }

        return result;
    }

    public Task createTask(long purchasedItems) {
    	return new Task(purchasedItems);
    }

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
