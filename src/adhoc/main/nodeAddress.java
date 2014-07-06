package adhoc.main;
import java.util.ArrayList;
public class nodeAddress {
	private static nodeAddress instance;
	private String[] ipArray;
	public static nodeAddress getInstance(){
		if(instance == null){
			instance = new nodeAddress();
			//ipArray = new String[size];
		}
		return instance;	
	}
	
	public void setArrayElement(ArrayList<String> element){
		ipArray = new String[element.size()];
		for(int i = 0 ; i < ipArray.length ; i++){
			ipArray[i] = element.get(i);
			ipArray[i] = ipArray[i].split("/")[1];
			//System.out.println(ipArray[i]);
		}
	}
	
	public String[] getIpArray(){
		return ipArray;
	}
	
}
