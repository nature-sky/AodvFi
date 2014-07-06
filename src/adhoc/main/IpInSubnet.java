package adhoc.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class IpInSubnet {
	//private boolean isInter;
	
	/* ********************************
	 *  make sure you install gksudo first  
	 *************************************/
	
	private String installCommand = "gksudo apt-get install nmap";
	private String nmapCommand = "";
	private Process proc ;
	private BufferedReader commandStreamReader;
	private String outputLine = "";
	private String subnet="";
	public static ArrayList<String> subnetList;	
	
	public IpInSubnet(String subNet){
		subnetList = new ArrayList<String>();
		for(int h = 0 ; h < 3 ; h++){
			subnet = subnet +subNet.split("\\.")[h]+ ".";				
		}	
		System.out.println(subnet + " subnet");
		nmapInit();
	}
	
	public void nmapInit(){
		nmapCommand = "nmap -sP " + subnet+"*" ;
		try {
			proc = Runtime.getRuntime().exec(nmapCommand);			
			commandStreamReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	        while((outputLine = commandStreamReader.readLine()) != null) {
			    System.out.print(outputLine + "\n");
			    searchSubnetList(outputLine);
			}
			
			proc.waitFor();
			System.out.println("finish");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block		
			/*
			 * install nmap first
			 */			
			System.out.println("You have to install nmap first, your install will start in 5 seconds");;
			try {
				Thread.sleep(5000);
				proc = Runtime.getRuntime().exec(installCommand);			
				commandStreamReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while((outputLine = commandStreamReader.readLine()) != null) {
				    System.out.print(outputLine + "\n");
				    
				}
				
				proc.waitFor();
				System.out.println("install successfully");
			} catch (InterruptedException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//call again
			nmapInit();
		}
	}
	
	public void searchSubnetList(String line){
	    if(line.contains("Nmap scan")){
	    	//0 Nmap
	    	//1 Scan
	    	//2 report
	    	//3 for
	    	//4 IP
	    	for(int i = 0 ; i <line.split(" ").length ; i++){
		    	if(!line.contains(InitConst.srcAddress) && line.split(" ")[i].contains(subnet)){	
		    		if(i == 4){
		    			subnetList.add(line.split(" ")[i]);
		    		}
		    		else if(i ==5){
		    			subnetList.add(line.split(" ")[i].split("\\(")[1].split("\\)")[0]);
		    		}
		    		
		    	}
	    	}

	    	System.out.println(subnetList);
	    }
		
	}
	
	
}
