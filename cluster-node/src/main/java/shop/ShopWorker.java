package shop;

import model.StatusResponse;
import model.StatusRequest;
import model.SerializationUtils;
import model.TaskRequest;
import model.TaskResponse;
import networking.OnRequestCallback;

public class ShopWorker implements OnRequestCallback {
    private static final String ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/task/info";

    public byte[] handleTaskRequest(byte[] requestPayload) {
        TaskRequest task = (TaskRequest) SerializationUtils.deserialize(requestPayload);
        TaskResponse result = createTaskResult(task);
        return SerializationUtils.serialize(result);
    }
    
    public byte[] handleInfoRequest(byte[] requestPayload) {
    	StatusRequest info = (StatusRequest) SerializationUtils.deserialize(requestPayload);
        StatusResponse result = createInfoResult(info);
        return SerializationUtils.serialize(result);
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
