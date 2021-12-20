package controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import model.SerializationUtils;
import model.TaskRequest;
import model.TaskResponse;
import model.Storage;
import networking.OnRequestCallback;


public class RequestHandler implements OnRequestCallback {
    private static final String ENDPOINT = "/orders";
    private final ObjectMapper objectMapper;
    private final Storage storage;

    public RequestHandler(Storage storage) {
        this.storage = storage;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public byte[] handleRequest(byte[] requestPayload) {
        TaskRequest task = (TaskRequest) SerializationUtils.deserialize(requestPayload);        
        TaskResponse result = createTaskResult(task);
        return SerializationUtils.serialize(result);
    }

    private TaskResponse createTaskResult(TaskRequest task) {
        System.out.println(String.format("Storage received order for %d items", task.getItemsPurchased()));
        
        long newValue = storage.getBananas() - task.getItemsPurchased();
        storage.setBananas(newValue);
        
        TaskResponse result = new TaskResponse();
        result.setItemsPurchased(task.getItemsPurchased());
        result.setItemsAvailable(storage.getBananas());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        
        return result;
    }

 
    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
