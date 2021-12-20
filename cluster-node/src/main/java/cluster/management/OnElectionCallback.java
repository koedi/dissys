package cluster.management;

import org.apache.zookeeper.ZooKeeper;

public interface OnElectionCallback {

    void onElectedToBeLeader();

    void onWorker(ZooKeeper zooKeeper);

}
