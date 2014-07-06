package adhoc.udp;



import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.naming.SizeLimitExceededException;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.DataExceedsMaxSizeException;
import adhoc.etc.Debug;
import adhoc.main.*;
public class UdpSender {
	private DatagramSocket datagramSocket;
	private int receiverPort = 8888;
	//private String subNet = "10.0.1.";
	public static String IPAddress = "";
	public static String IPAddress2 = "";
	private String subnet= "";
//	private static int[] ipsrc = new int[999];
//	private int srcindex = 0; // 10.0.1.86
								// ipsrc[0]=10
								// ipsrc[1]=0
	  							// ipsrc[2]=1
	
	public UdpSender(String srcIpAddress) throws SocketException, UnknownHostException, BindException{
		
		try{
	    datagramSocket = new DatagramSocket(6666);
	    System.out.println(srcIpAddress+ "6666");
		}catch(BindException e){
		    datagramSocket = new DatagramSocket(7777);
		    System.out.println(srcIpAddress+ "7777");
		}
	}

	/**
	 * Sends data using the UDP protocol to a specific receiver
	 * @param destinationNodeID indicates the ID of the receiving node. Should be a positive integer.
	 * @param data is the message which is to be sent. 
	 * @throws IOException 
	 * @throws SizeLimitExceededException is thrown if the length of the data to be sent exceeds the limit
	 */
	public boolean sendPacket(String destinationNodeID, byte[] data) throws IOException, DataExceedsMaxSizeException{
		if(data.length <= Constants.MAX_PACKAGE_SIZE) {
//			NMAP nmap = new NMAP();
//			IPAddress = nmap.checkIPAddress();
//			String srcsubNet = "";
//		    for(int h = 0 ; h < 3 ; h++){
//				srcsubNet = srcsubNet + IPAddress.split("\\.")[h]+ ".";
//			}
//		    
			subnet="";
			for(int h = 0 ; h < 3 ; h++){
				subnet = subnet +InitConst.srcAddress.split("\\.")[h]+ ".";				
			}	
		    Debug.print("srcsubNet"+subnet);
		    
			//Client broadcast control start 
		    DatagramPacket sendPacket = null;
		    int destlastIPAddress = Integer.parseInt(destinationNodeID.split("\\.")[3]);
		    String destsubNet = "";
		    for(int h = 0 ; h < 3 ; h++){
				destsubNet = destsubNet + destinationNodeID.split("\\.")[h]+ ".";
			}
		    Debug.print("destsubNet"+destsubNet);
		    InetAddress bcastIPAddress;
		    //do we have a packet to be broadcasted?
		    //if(destsubNet.equals(subnet)) {
		        bcastIPAddress = InetAddress.getByName(destsubNet + destlastIPAddress);
		    	if(destlastIPAddress == Constants.BROADCAST_ADDRESS){
					datagramSocket.setBroadcast(true);
					sendPacket = new DatagramPacket(data, data.length, bcastIPAddress, receiverPort+1);
				} 
		    	else {
					datagramSocket.setBroadcast(false);
					sendPacket = new DatagramPacket(data, data.length, bcastIPAddress, receiverPort);
				}
		    //} 
//		    else {
//		    	bcastIPAddress = InetAddress.getByName(srcsubNet + Constants.BROADCAST_ADDRESS);
//		    	datagramSocket.setBroadcast(true);
//				sendPacket = new DatagramPacket(data, data.length, bcastIPAddress, receiverPort+1);
//		    }
		    //Client broadcast control end
		    
		    //Client sendPacket
		    if(sendPacket != null) {
		    	datagramSocket.send(sendPacket);
				return true;
		    }
		    else
		    	return false;
        } else {
				throw new DataExceedsMaxSizeException();
	    } 
		
		     //InetAddress IPAddress = InetAddress.getByName(ipsrc[0]+"."+ipsrc[1]+"."+ipsrc[2]+"."+destinationNodeID);
				
//			 DatagramPacket sendPacket;
//			 if(destinationNodeID == Constants.BROADCAST_ADDRESS){
//				datagramSocket.setBroadcast(true);
//				sendPacket = new DatagramPacket(data, data.length, IPAddress, receiverPort+1);
//			 }else {
//				datagramSocket.setBroadcast(false);
//				sendPacket = new DatagramPacket(data, data.length, IPAddress, receiverPort);
//			 }
//			 datagramSocket.send(sendPacket);
//				return true;
//			 } else {
//				throw new DataExceedsMaxSizeException();
//			 } 
	}
	
	public void closeSoket(){
		datagramSocket.close();
	}

}
