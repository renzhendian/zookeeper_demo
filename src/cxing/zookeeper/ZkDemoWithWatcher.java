package cxing.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZkDemoWithWatcher {

	public static void main(String[] args){
		ZkDemoWithWatcher entity = new ZkDemoWithWatcher();
		try {
			ZooKeeper zk = new ZooKeeper("localhost:2181",6000,null);
			String path = "/user01";
			entity.watchNode(zk, path);
			System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void watchNode(ZooKeeper zk,String path){
	    try {
			List<String> nodeList = zk.getChildren(path, new Watcher(){
				@Override
				public void process(WatchedEvent event){
					if(event.getType().equals(Event.EventType.NodeChildrenChanged)){
						watchNode(zk,path);
					}
				}
			});
			System.out.println("*********************************");
			for (String node : nodeList) {
	            byte[] b = zk.getData(path + "/" + node,false,null); 
	            System.out.println(new String(b));
	        }
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
