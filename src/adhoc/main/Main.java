package adhoc.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class Main {
	private ArrayList<String> networkList;
	private String subnet = "";
	private FunctionalTest[] functionaltest;
	private Thread[] thread;

	public static void main(String[] args){		
		Main main = new Main();
		main.init();
	}	
	
	private void init(){
		getWirelessIp();
		nodeAddress.getInstance().setArrayElement(networkList);
		//intermediate node then the length will more than one, that is, there are 
		//at least two ips in one computer, we regard this as intermediate node(tricky way)
	
		if(nodeAddress.getInstance().getIpArray().length > 1){
			InitConst.isInter  = true;			
		}				
		//if is intermediate point then we have to establish multiple object. 
		//Otherwise, we only have to new one object(source and destination)
		functionaltest= new FunctionalTest[nodeAddress.getInstance().getIpArray().length];
		thread = new Thread[nodeAddress.getInstance().getIpArray().length];
		for(int i = 0 ; i < nodeAddress.getInstance().getIpArray().length ; i++){
			System.out.println(nodeAddress.getInstance().getIpArray().length);
			InitConst.srcAddress = nodeAddress.getInstance().getIpArray()[i];
			System.out.println(InitConst.srcAddress +"  33333333");
			functionaltest[i] = new FunctionalTest(InitConst.srcAddress);
			thread[i] = new Thread(functionaltest[i]);
			thread[i].start();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//dataShared.getInstance().addIP(subNet + srcAddress);
			//System.out.println(subNet);
			subnet ="";
		}
	}
	
	private void getWirelessIp(){
        Enumeration<NetworkInterface> nets;
        networkList = new ArrayList<String>();
        int number = 0;
        try {
            nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netint : Collections.list(nets)){
            	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
           		for (InetAddress inetAddress : Collections.list(inetAddresses)) {	 
           			if(number %2 ==1){
           				networkList.add(inetAddress.toString());       				
           			}
           			number ++;
           			//System.out.println(number);
           		}	                
            }
            networkList.remove(networkList.get(networkList.size()-1));
        }catch(Exception e){
        	//
        }   
        /*
        for(int i = 0 ;i < networkList.size() ; i++){
        	System.out.println(networkList.get(i));
        }
        */
	}
}
