package adhoc.main;
import static java.lang.System.out;

import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import adhoc.aodv.Node;
import adhoc.aodv.ObserverConst;
import adhoc.aodv.Node.MessageToObserver;
import adhoc.aodv.Node.PacketToObserver;
import adhoc.aodv.exception.InvalidNodeAddressException;
import adhoc.etc.Debug;
import adhoc.udp.UdpSender;

public class FunctionalTest implements Observer , Runnable {
	private Node node;
	private volatile boolean readyToResume = false;
	private String myAddress;
	private IpInSubnet ipSubnet ;
	//private volatile boolean interReadyToSend = false;
	private int interPacketId = 1;
	ReadWriteLock lock = new ReentrantReadWriteLock();  
	Read read = new Read(lock);
	Timer readTimer = new Timer();
	Timer writeTimer;
	
//	public static void main(String[] args) {
//		getWirelessIp();
//		nodeAddress.getInstance().setArrayElement(networkList);
//		//intermediate node then the length will more than one, that is, there are 
//		//at least two ips in one computer, we regard this as intermediate node(tricky way)
//		
//		if(nodeAddress.getInstance().getIpArray().length > 1){
//			InitConst.isInter  = true;			
//		}				
//		//if is intermediate point then we have to establish multiple object. 
//		//Otherwise, we only have to new one object(source and destination)
//		for(int i = 0 ; i < nodeAddress.getInstance().getIpArray().length ; i++){
//			//System.out.println(nodeAddress.getInstance().getIpArray().length);
//			InitConst.srcAddress = nodeAddress.getInstance().getIpArray()[i];
//			for(int h = 0 ; h < 3 ; h++){
//				subnet = subnet +nodeAddress.getInstance().getIpArray()[i].split("\\.")[h]+ ".";				
//			}	
//			new FunctionalTest(InitConst.srcAddress);
//			//dataShared.getInstance().addIP(subNet + srcAddress);
//			//System.out.println(subNet);
//			subnet ="";
//		}		
//	}
//	
	

	public FunctionalTest(String myAddress ) {
		this.myAddress = myAddress;	
	}
	
	public void run(){
		try {
			Debug.setDebugStream(System.out);
			node = new Node(myAddress);
		} catch (BindException e) {
			e.printStackTrace();
		} catch (InvalidNodeAddressException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		node.addObserver(this);
		node.startThread();

		if (myAddress == InitConst.srcAddress) {
			System.out.println("A");
			SourceActionsInFuncTestWithTwoNodes();
		}
	}
	
//	private static void getWirelessIp(){
//        Enumeration<NetworkInterface> nets;
//        networkList = new ArrayList<String>();
//        int number = 0;
//        try {
//            nets = NetworkInterface.getNetworkInterfaces();
//
//            for (NetworkInterface netint : Collections.list(nets)){
//            	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
//           		for (InetAddress inetAddress : Collections.list(inetAddresses)) {	 
//           			if(number %2 ==1){
//           				networkList.add(inetAddress.toString());       				
//           			}
//           			number ++;
//           			//System.out.println(number);
//           		}	                
//            }
//            networkList.remove(networkList.get(networkList.size()-1));
//        }catch(Exception e){
//        	//
//        }   
//        /*
//        for(int i = 0 ;i < networkList.size() ; i++){
//        	System.out.println(networkList.get(i));
//        }
//        */
//	}
	
	

	private void destActionsInFuncTestWithTwoNodes() {
		node.stopThread();
		try {
			synchronized (this) {
				this.wait(10000);
			}
		} catch (InterruptedException e) {

		}
		node.startThread();

		//node.sendData(1, InitConst.srcAddress,
			//	new String("terminate test now").getBytes());

		node.stopThread();
	}

	private void SourceActionsInFuncTestWithTwoNodes() {
//		if(nodeAddress.getInstance().getIpArray().length > 1){
//			interReadyToSend =true;
//		}		
		System.out.println("B");

		try {
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}
			System.out.println("C");
			/*
			 * 
			 * 
			 * 
			 * send data
			 */
			///when situation is source address 
//			if(!InitConst.isInter && !InitConst.isDest  ){
//				ipSubnet = new IpInSubnet(InitConst.srcAddress);
//				
//			}
			
//			if(InitConst.isDest == false && InitConst.isInter == false){
//				for(int i = 0  ;  i < InitConst.sendData.length ; i++){
//					for(int j = 0 ; j<  IpInSubnet.subnetList.size() ; j++){
//						node.sendData(i+"", IpInSubnet.subnetList.get(j), new String(InitConst.sendData[i]).getBytes());				
//					}
//				}
//			}
			if(!InitConst.isInter && !InitConst.isDest  ){
				ipSubnet = new IpInSubnet(InitConst.srcAddress);
				System.out.println(InitConst.srcAddress+ "       3");
				
			}
			if(InitConst.isDest == false && InitConst.isInter == false){
				for(int i = 0  ;  i < InitConst.sendData.length ; i++){
					for(int j = 0 ; j<  IpInSubnet.subnetList.size() ; j++){				
						node.sendData(i+"", IpInSubnet.subnetList.get(j), new String(InitConst.sendData[i]).getBytes());				
					}
				}
			}
			
			if(InitConst.isDest == false && InitConst.isInter == true){
				//while(true){
					ipSubnet = new IpInSubnet(myAddress);
					readTimer.schedule(read, 0 , 300);
				//}
			}
			//readyToResume = false;
			//interReadyToSend = true;
			
			/*
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}
			node.sendData(5, -1,
					new String(" invalid address test ").getBytes());

			byte[] data = new byte[54001];
			generator.nextBytes(data);
			node.sendData(6, 255, data);
			readyToResume = false;
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}
			generator.nextBytes(data);
			node.sendData(7, 2, data);
			node.sendData(8, 2, new String(" dest stop now ").getBytes());
			readyToResume = false;
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}
			*/
			//node.stopThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		MessageToObserver msg = (MessageToObserver) arg1;
		String userPacketID, destination;
		int type = msg.getMessageType();
		switch (type) {
		case ObserverConst.ROUTE_ESTABLISHMENT_FAILURE:
			// Note : any m e s s a g e s t h a t had same d e s t i n a t i o n
			// has been removed from sending
			String unreachableDestinationAddrerss = (String)msg.getContainedData();
			if (readyToResume == false && unreachableDestinationAddrerss.equals("5")) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}
			}
			Debug.print(" FuncTest : ROUTE_ESTABLISHMENT_FAILURE notification received - Unreachable node : "
					+ unreachableDestinationAddrerss);
			break;
		case ObserverConst.DATA_RECEIVED:
			byte[] data = (byte[]) msg.getContainedData();
			//////////////////////////////
			writeTimer = new Timer();
			Write write = new Write(lock);
			write.setData(data);
			writeTimer.schedule(write , 0);		
			//dataShared.getInstance().addData(new String(data));
			
			/////////////////////////////////
			String senderAddress = ((PacketToObserver) msg)
					.getSenderNodeAddress();
			Debug.print(" FuncTest : DATA_RECEIVED notification received - from destAdr : "
					+ senderAddress + " containing : " + new String(data));
			
//			if(!readyToResume){
//				interReadyToSend = false;
//			}
//			else if (readyToResume){
//				interReadyToSend = false;
//				synchronized (this) {
//					this.notify();
//				}
//			}
			/*******************************************************/

			/*******************************************************/
			if (senderAddress.equals(InitConst.srcAddress)
					&& (new String(data)).equals(" dest stop now ")) {
				destActionsInFuncTestWithTwoNodes();
			}
			if (senderAddress.equals(InitConst.destAddress)
					&& (new String(data).equals(" terminate test now "))) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}
			}
			break;
		case ObserverConst.DATA_SENT_SUCCESS:
			userPacketID =  (String)msg.getContainedData();
			Debug.print(" FuncTest : DATA_SENT_SUCCESS notification received - packetID : "
					+ userPacketID);
			break;
		case ObserverConst.INVALID_DESTINATION_ADDRESS:
			userPacketID = (String)msg.getContainedData();
			Debug.print(" FuncTest : INVALID_DESTINATION_ADDRESS notification received - packetID : "
					+ userPacketID);
			break;
		
			
		//still consider to delete this code
		case ObserverConst.DATA_SIZE_EXCEEDES_MAX:
			userPacketID = (String) msg.getContainedData();
			Debug.print(" FuncTest : DATA_SIZE_EXCEEDES_MAX notification received - packetID : "
					+ userPacketID);
			if (readyToResume == false
					&& (userPacketID .equals("5") || userPacketID.equals("6"))) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}

			}
			break;
		case ObserverConst.ROUTE_INVALID:
			destination = (String)msg.getContainedData();
			Debug.print(" FuncTest : ROUTE_INVALID notification received - for destAdr : "
					+ destination);
			break;
		case ObserverConst.ROUTE_CREATED:
			destination = (String)msg.getContainedData();
			System.out.println(destination+"    314");
			System.out.println(readyToResume+"    315");
			if (readyToResume == false) {
				readyToResume = true;
				System.out.println("316");
//				readTimer.schedule(read, 0);
//				if(!interReadyToSend){
//					synchronized (this) {
//					this.notify();
//				}
				//}
				synchronized (this) {
					this.notify();
				}
			}
			Debug.print(" FuncTest : ROUTE_CREATED notification received - to node : "
					+ destination);
			break;
			
		default:
			break;
		}
	}


class Read extends TimerTask{
	ReadWriteLock lock;
	byte[] data ;
	Read(ReadWriteLock lock){
		this.lock = lock;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
//        try {  
//        	synchronized (this) {
//        		this.notify();
//        	}
//        	
        	lock.readLock().lock();         	
        	for(int i = dataShared.getInstance().getDataReadInt() ; i < dataShared.getInstance().getData().size() ; i++ ){
        		for(int j = 0 ; j<  IpInSubnet.subnetList.size() ; j++){
        			System.out.println("357");
        			node.sendData(interPacketId+"", IpInSubnet.subnetList.get(j), dataShared.getInstance().getData().get(i).getBytes());
        			interPacketId ++;
        		}			
        	}
        	dataShared.getInstance().setDataReadNumber(dataShared.getInstance().getData().size());
        	lock.readLock().unlock();
//        }catch (Exception e) {  
//            e.printStackTrace();  
//        }     
	}
}


class Write extends TimerTask{
	ReadWriteLock lock;
	byte[] data;
	Write(ReadWriteLock lock){
		this.lock = lock;
	}
	void setData(byte[] data){
		this.data = data;
	}
	@Override
	public void run() {
		//try {
				// TODO Auto-generated method stub
				lock.writeLock().lock();
				System.out.println("5555555555555555555555");
				dataShared.getInstance().addData(new String(data));
				lock.writeLock().unlock();  
			//}
		//}
		//catch(Exception e){}
	}
}
}
	
	

