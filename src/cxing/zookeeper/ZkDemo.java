package cxing.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZkDemo {

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException{
		ZooKeeper zk = null;
		zk = new ZooKeeper("localhost:2181",6000, null);
		String path = "/user01";
		zk.delete(path, -1);
		zk.create(path,null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		String subPath01="/user01/s01";
		byte[] data1 = "test11".getBytes();
		zk.create(subPath01,data1,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
		String subPath02="/user01/s02";
		byte[] data2 = "test22".getBytes();
		zk.create(subPath02,data2,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
		List<String> nodeList01 = zk.getChildren(path, false);
        for (String node : nodeList01) {
            byte[] b = zk.getData(path + "/" + node,false,null); 
            System.out.println(new String(b));
        }
        System.out.println("*********************************");
		zk.delete(subPath01, -1);
		List<String> nodeList02 = zk.getChildren(path, false);
        for (String node : nodeList02) {
            byte[] b = zk.getData(path + "/" + node,false,null); 
            System.out.println(new String(b));
        }
	}
	
}
