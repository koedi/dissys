package networking;

public interface OnRequestCallback {
    public byte[] handleTaskRequest(byte[] requestPayload);
    public String getEndpoint();
}
