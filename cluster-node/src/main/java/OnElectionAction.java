import cluster.management.Lock;
import cluster.management.OnElectionCallback;
import cluster.management.ServiceRegistry;
import networking.WebClient;
import networking.WebServer;
import shop.ShopCoordinator;
import shop.ShopWorker;

import org.apache.zookeeper.KeeperException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OnElectionAction implements OnElectionCallback {
    private final ServiceRegistry workersServiceRegistry;
    private final ServiceRegistry coordinatorsServiceRegistry;
    private final Lock lock;
    private final int port;
    private WebServer webServer;

    public OnElectionAction(ServiceRegistry workersServiceRegistry,
                            ServiceRegistry coordinatorsServiceRegistry,
                            Lock lock,
                            int port) {
        this.workersServiceRegistry = workersServiceRegistry;
        this.coordinatorsServiceRegistry = coordinatorsServiceRegistry;
        this.lock = lock;
        this.port = port;
    }

    @Override
    public void onElectedToBeLeader() {
        workersServiceRegistry.unregisterFromCluster();
        workersServiceRegistry.registerForUpdates();

        if (webServer != null) {
            webServer.stop();
        }

        ShopCoordinator searchCoordinator = new ShopCoordinator(workersServiceRegistry, new WebClient());
        webServer = new WebServer(port, searchCoordinator);
        webServer.startServer();

        try {
            String currentServerAddress =
                    String.format("http://%s:%d%s", InetAddress.getLocalHost().getCanonicalHostName(), port, searchCoordinator.getEndpoint());
            coordinatorsServiceRegistry.registerToCluster(currentServerAddress);
        } catch (InterruptedException | UnknownHostException | KeeperException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onWorker() {
        ShopWorker searchWorker = new ShopWorker(new WebClient(), lock);
        if (webServer == null) {
            webServer = new WebServer(port, searchWorker);
            webServer.startServer();
        }

        try {
            String currentServerAddress =
                    String.format("http://%s:%d%s", InetAddress.getLocalHost().getCanonicalHostName(), port, searchWorker.getEndpoint());

            workersServiceRegistry.registerToCluster(currentServerAddress);
        } catch (InterruptedException | UnknownHostException | KeeperException e) {
            e.printStackTrace();
            return;
        }
    }
}
