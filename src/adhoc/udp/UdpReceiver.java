package adhoc.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import adhoc.aodv.Receiver;



/**
 * Class running as a separate thread, and responsible for receiving data packets over the UDP protocol.
 * @author Rabie
 *
 */
public class UdpReceiver implements Runnable{
	private Receiver parent;
	private DatagramSocket datagramSocket;
	private UdpBroadcastReceiver udpBroadcastReceiver;
	private volatile boolean keepRunning = true;
	private Thread udpReceiverthread;
	public static String IPAddress = "read IP error!";
	private static int[] ipsrc = new int[999];     // 10.0.1.86
													  // ipsrc[0]=10
													  // ipsrc[1]=0
	  												  // ipsrc[2]=1
													  // ipsrc[3]=86
	public UdpReceiver(Receiver parent, String nodeAddress) throws SocketException, UnknownHostException, BindException{
//		int index = 0;                            // 30 ~ 77 自動讀取網域 10.0.1.x
//		String fullip = ""; 
//		NMAP nmap = new NMAP();
//		IPAddress=nmap.checkIPAddress();
//		for(int i=0;i<IPAddress.length();i++)
//		 { 
//			 if(IPAddress.substring(i,i+1).equals(" "))
//			 {
//				 fullip=IPAddress.substring(0,i);
//				 i=IPAddress.length();
//			 }
//		 }
//		int test=0;
//		for(int i=0;i<fullip.length();i++)
//		 { 
//			 if(fullip.substring(i,i+1).equals("."))
//			 {
//				 ipsrc[index]=Integer.parseInt(fullip.substring(test,i));
//				 index++;
//				 test=i+1;
//			 }
//			 if(index==3)
//			 {
//				 int temp = fullip.length()-test;
//				 //ipsrc[index]=Integer.parseInt(fullip.substring(test,test+2));
//				 if(temp==1){
//					 if(fullip.substring(test,test+1).equals("1") || fullip.substring(test,test+1).equals("2") || fullip.substring(test,test+1).equals("3") || fullip.substring(test,test+1).equals("4") ||
//							 fullip.substring(test,test+1).equals("5") || fullip.substring(test,test+1).equals("6") || fullip.substring(test,test+1).equals("7") ||
//							 fullip.substring(test,test+1).equals("8") || fullip.substring(test,test+1).equals("9") || fullip.substring(test,test+1).equals("0")){
//						 ipsrc[index]=Integer.parseInt(fullip.substring(test,test+1));
//					 }
//				 }
//				 else if(temp==2){
//					 if(fullip.substring(test+1,test+2).equals("1") || fullip.substring(test+1,test+2).equals("2") || fullip.substring(test+1,test+2).equals("3") || fullip.substring(test+1,test+2).equals("4") ||
//							 fullip.substring(test+1,test+2).equals("5") || fullip.substring(test+1,test+2).equals("6") || fullip.substring(test+1,test+2).equals("7") ||
//							 fullip.substring(test+1,test+2).equals("8") || fullip.substring(test+1,test+2).equals("9") || fullip.substring(test+1,test+2).equals("0")){
//						 ipsrc[index]=Integer.parseInt(fullip.substring(test,test+2));
//					 }
//				 }
//				 else if(temp==3){
//					 if(fullip.substring(test+2,test+3).equals("1") || fullip.substring(test+2,test+3).equals("2") || fullip.substring(test+2,test+3).equals("3") || fullip.substring(test+2,test+3).equals("4") ||
//							 fullip.substring(test+2,test+3).equals("5") || fullip.substring(test+2,test+3).equals("6") || fullip.substring(test+2,test+3).equals("7") ||
//							 fullip.substring(test+2,test+3).equals("8") || fullip.substring(test+2,test+3).equals("9") || fullip.substring(test+2,test+3).equals("0")){
//						 ipsrc[index]=Integer.parseInt(fullip.substring(test,test+3));
//					 }
//				 }		 
//			 }
//		 }
		this.parent = parent;
		this.IPAddress = nodeAddress;
		//datagramSocket = new DatagramSocket(new InetSocketAddress(ipsrc[0]+"."+ipsrc[1]+"."+ipsrc[2]+"."+ nodeAddress ,8888));
	    System.out.println(IPAddress+ "7575");
		datagramSocket = new DatagramSocket(new InetSocketAddress(IPAddress ,7575));
		datagramSocket.setBroadcast(true);
		try{
		udpBroadcastReceiver = new UdpBroadcastReceiver(7575);
		}catch(BindException e) {
		    System.out.println(IPAddress+ "7777");
			udpBroadcastReceiver = new UdpBroadcastReceiver(7777);
		}
	}
	
	public void startThread(){
		keepRunning = true;
		udpBroadcastReceiver.startBroadcastReceiverthread();
		udpReceiverthread = new Thread(this);
		udpReceiverthread.start();
	}
	
	public void stopThread(){
		keepRunning = false;
		udpBroadcastReceiver.stopBroadcastThread();
		udpReceiverthread.interrupt();
	}
	
	public void run(){
		while(keepRunning){
			try {
				// 52kb buffer
				byte[] buffer = new byte[52000];
				DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);
	
				datagramSocket.receive(receivePacket);
			    byte[] result = new byte[receivePacket.getLength()];
			    System.arraycopy(receivePacket.getData(), 0, result, 0, receivePacket.getLength());
			    
			    String ip = receivePacket.getAddress().toString();
			    
			    String address = "";
			    address = ip.substring(1, ip.length());
			    parent.addMessage(address,result);  
//			    if(!address.equals(IPAddress)){
//			    	parent.addMessage(address,result);  
//			    }   
			} catch (IOException e) {

			}
		}
	}
	
	private class UdpBroadcastReceiver implements Runnable{
		private DatagramSocket brodcastDatagramSocket;
		private volatile boolean keepBroadcasting = true;
		private Thread udpBroadcastReceiverThread;
		
		public UdpBroadcastReceiver( int receiverPort) throws SocketException, BindException{
			brodcastDatagramSocket = new DatagramSocket(receiverPort+1);
		}
		
		public void startBroadcastReceiverthread(){
			keepBroadcasting = true;
			udpBroadcastReceiverThread = new Thread(this);
			udpBroadcastReceiverThread.start();
		}
		
		private void stopBroadcastThread(){
			keepBroadcasting = false;
			udpBroadcastReceiverThread.interrupt();
		}
		
		public void run(){
			while(keepBroadcasting){
				try {
					// 52kb buffer
					byte[] buffer = new byte[52000];
					DatagramPacket brodcastReceivePacket = new DatagramPacket(buffer,buffer.length);
	
					brodcastDatagramSocket.receive(brodcastReceivePacket);
	
				    byte[] result = new byte[brodcastReceivePacket.getLength()];
				    System.arraycopy(brodcastReceivePacket.getData(), 0, result, 0, brodcastReceivePacket.getLength());
				    
				    
				    String ip = brodcastReceivePacket.getAddress().toString();
				    
				    String address = "";
				    address = ip.substring(1, ip.length());
				    parent.addMessage(address,result);  
//				    if(!address.equals(IPAddress)){
//				    	parent.addMessage(address,result);  
//				    }
				    
				} catch (IOException e) {
					
				}
			}
		}
	}
	
}
