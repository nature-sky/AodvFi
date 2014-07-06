package adhoc.main;
import java.net.DatagramPacket;
import java.util.ArrayList;


public class dataShared {
	private static dataShared instance;
	private ArrayList<String> dataList;
	private int dataAlreadyRead = 0;
	//private String[] data;
	public static dataShared getInstance(){
		if(instance == null){
			instance = new dataShared();
			instance.newDataList();
			//ipArray = new String[size];
		}
		return instance;
	}	
	public void newDataList(){
		dataList = new ArrayList<String>();
	}
//	public void addIP(String IP){
//		allIP.add(IP);
//	}
	
	public void addData(String data){
		dataList.add(data);
	}
	
	public ArrayList<String> getData(){
		return dataList;
	}
	
	public void setDataReadNumber(int number){
		this.dataAlreadyRead = number;
	}
	
	public void incDataRead(){
		dataAlreadyRead ++;
	}
	
	public int getDataReadInt(){
		return dataAlreadyRead;
	}
}
