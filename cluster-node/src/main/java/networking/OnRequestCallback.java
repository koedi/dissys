package networking;

public interface OnRequestCallback {
    byte[] handleTaskRequest(byte[] requestPayload);
    byte[] handleInfoRequest(byte[] requestPayload);

    String getEndpoint();
    String getStatusEndpoint();
}
