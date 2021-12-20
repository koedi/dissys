package shop;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.protobuf.InvalidProtocolBufferException;

import cluster.management.ServiceRegistry;
import model.frontend.ShopRequest;
import model.frontend.ShopResponse;
import model.proto.ShopModel;
import networking.OnRequestCallback;
import networking.WebClient;

public class RequestHandler implements OnRequestCallback {
    private static final String ENDPOINT = "/shop";
    private final ObjectMapper objectMapper;
    private final WebClient client;
    private final ServiceRegistry searchCoordinatorRegistry;

    public RequestHandler(ServiceRegistry searchCoordinatorRegistry) {
        this.searchCoordinatorRegistry = searchCoordinatorRegistry;
        this.client = new WebClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public byte[] handleRequest(byte[] requestPayload) {
        try {
            ShopRequest request = objectMapper.readValue(requestPayload, ShopRequest.class);

            ShopResponse frontendSearchResponse = createFrontendResponse(request);

            return objectMapper.writeValueAsBytes(frontendSearchResponse);
            
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private ShopResponse createFrontendResponse(ShopRequest frontendSearchRequest) {
        ShopModel.Response clusterResponse = sendRequestToSearchCluster(frontendSearchRequest.getPurchaseQuery());

        ShopResponse.ResultInfo results = new ShopResponse.ResultInfo(clusterResponse.getItemsAvailable(), clusterResponse.getItemsPurchased());

        return new ShopResponse(results);
    }

    
    private ShopModel.Response sendRequestToSearchCluster(long purchaseQuery) {
        ShopModel.Request request = ShopModel.Request.newBuilder()
                .setPurchaseQuery(purchaseQuery)
                .build();

        try {
            String coordinatorAddress = searchCoordinatorRegistry.getRandomServiceAddress();
            if (coordinatorAddress == null) {
                System.out.println("Cluster Coordinator is unavailable");
                return ShopModel.Response.getDefaultInstance();
            }

            byte[] payloadBody = client.sendTask(coordinatorAddress, request.toByteArray()).join();

            return ShopModel.Response.parseFrom(payloadBody);
        } catch (InterruptedException | KeeperException | InvalidProtocolBufferException e) {
            e.printStackTrace();
            return ShopModel.Response.getDefaultInstance();
        }
    }
    
    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
