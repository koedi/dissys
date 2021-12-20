import networking.WebServer;
import controller.RequestHandler;

import java.io.IOException;
import model.Storage;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        int currentServerPort = 9001;
        if (args.length == 1) {
            currentServerPort = Integer.parseInt(args[0]);
        }

        Storage storage = new Storage(1000);
        RequestHandler storageHandler = new RequestHandler(storage);
        WebServer webServer = new WebServer(currentServerPort, storageHandler);
        webServer.startServer();

        System.out.println("Server is listening on port " + currentServerPort);

    }

}
