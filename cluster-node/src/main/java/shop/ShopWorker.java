package shop;

import model.Result;
import model.SerializationUtils;
import model.Task;
import networking.OnRequestCallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShopWorker implements OnRequestCallback {
    private static final String ENDPOINT = "/task";

    public byte[] handleRequest(byte[] requestPayload) {
        Task task = (Task) SerializationUtils.deserialize(requestPayload);
        Result result = createResult(task);
        return SerializationUtils.serialize(result);
    }

    private Result createResult(Task task) {
        System.out.println(String.format("Received order for %d items", task.getItemsPurchased()));
        
        Result result = new Result();
        result.setItemsAvailable(100);
        result.setItemsPurchased(task.getItemsPurchased());

        return result;
    }
    
    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
