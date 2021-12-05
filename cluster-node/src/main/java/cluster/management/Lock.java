package cluster.management;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lock implements Watcher {
    private static final String LOCK_NAMESPACE = "/lock_write";
    private final ZooKeeper zooKeeper;

    public Lock(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    private synchronized String createWriteLock() throws KeeperException, InterruptedException {
        String lockNodePrefix = LOCK_NAMESPACE + "/n_";
        String lockNodeFullPath = zooKeeper.create(
            lockNodePrefix, 
            "a".getBytes(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE, 
            CreateMode.EPHEMERAL_SEQUENTIAL
        );
        return lockNodeFullPath;
        
    }


    public synchronized String aquireWriteLock() throws KeeperException, InterruptedException {
      String writeLock = createWriteLock();

      String[] writeLockSplitted = writeLock.split("/");
      String writeLockIdentifier = writeLockSplitted[writeLockSplitted.length - 1];
      boolean exit = false;
      int inLoop = 0;
      while (!exit) {
        inLoop++;
        
        List<String> children = zooKeeper.getChildren(LOCK_NAMESPACE, false);
        Collections.sort(children);
        System.out.println("Current lock");
        System.out.println(writeLock);
        System.out.println("Cildren:");
        for (String child: children) {
            System.out.println(child);
        }
        
        String smallestChild = children.get(0);
        String smallestChildFullPath = LOCK_NAMESPACE + "/" + smallestChild;
        System.out.println("smallest child full path");
        System.out.println(smallestChildFullPath);
        
        if (smallestChildFullPath.equals(writeLock)) {
            
            exit = true;
            return writeLock;

        } else {
            try {
                int oneSmallerIndex = children.indexOf(writeLockIdentifier) - 1;
                String oneSmallerIdentifier = children.get(oneSmallerIndex);
                String oneSmallerFullPath = LOCK_NAMESPACE + "/" + oneSmallerIdentifier;
                System.out.println(oneSmallerFullPath);
                System.out.println("didnt get write lock");
                TimeUnit.SECONDS.sleep(1);

                // zooKeeper.exists(oneSmallerFullPath, true);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("error");
               
            }
        }
      }

      return "";

      
    }

    public synchronized void releaseWriteLock(String writeLock) throws KeeperException, InterruptedException {
      System.out.println("Releasing lock");
      zooKeeper.delete(writeLock, -1);
      System.out.println("Released lock " + writeLock);
      
    }

    

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case NodeDeleted:
                System.out.println("On lock process");
        }
    }
}