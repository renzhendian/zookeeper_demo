package cxing.zookeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZkDemo {

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException{
		ZooKeeper zk = null;
		zk = new ZooKeeper("localhost:2181",6000, null);
		String path = "/user01";
		ZkDemo zkDemo=new ZkDemo();
		//清空以前的残留数据
		zkDemo.deleteNode(zk, path);
		//创建根路径
		zkDemo.createNode(zk, path, null);
		//两个子路径
		zkDemo.createNode(zk, "/user01/s01", "sub01");
		zkDemo.createNode(zk, "/user01/s02", "sub02");
		zkDemo.readNode(zk, path);
        //修改
		zkDemo.updateNode(zk, "/user01/s02", "sub02修改");		
		zkDemo.readNode(zk, path);
		//删除
		zkDemo.deleteNode(zk, "/user01/s02");
		zkDemo.readNode(zk, path);
	}
	
	public String createNode(ZooKeeper zk,String path,String data) throws KeeperException, InterruptedException{
		byte[] bts=null;
		if(data!=null) bts=data.getBytes();
		String actualPath=zk.create(path,bts,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
	    return actualPath;
	}
	
	public void updateNode(ZooKeeper zk,String path,String data) throws KeeperException, InterruptedException{
		byte[] bts=null;
		if(data!=null) bts=data.getBytes();
		Stat stat = zk.exists(path, true);
		if(stat!=null){
			zk.setData(path, bts, -1);
		}
	}
	
	public void deleteNode(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		Stat stat = zk.exists(path, true);
		if(stat!=null){
			List<String> nodeList = zk.getChildren(path, false);
	        for (String node : nodeList) {
	        	zk.delete(path + "/" + node, -1);
	        }
	        zk.delete(path, -1);
		}
	}
	
	public Map<String,String> readNode(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		System.out.println("...读取：");
		Map<String,String> map=new HashMap<String,String>();
		List<String> nodeList01 = zk.getChildren(path, false);
        for (String node : nodeList01) {
        	String p=path + "/" + node;
            byte[] b = zk.getData(p,false,null); 
            String d=new String(b);
            map.put(p, d);
            System.out.println(d);
        }
        return map;
	}
}
