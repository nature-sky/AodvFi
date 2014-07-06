package adhoc.aodv.routes;

import java.util.ArrayList;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.RouteNotValidException;
import adhoc.etc.Debug;

public class ForwardRouteEntry extends RouteEntry {

    private ArrayList<String> precursorNodes = new ArrayList<String>();
    private volatile boolean isValid = true;
    private String nextHop;
    
    public ForwardRouteEntry(String destAddress, String nextHopAddress, int hopCount, int destSeqNum, ArrayList<String>  precursorNodes) throws RouteNotValidException {
    	super(hopCount, destSeqNum, destAddress);
    	int nextHoplastIPAddress = Integer.parseInt(nextHopAddress.split("\\.")[3]);
    	if(nextHoplastIPAddress <= Constants.MAX_VALID_NODE_ADDRESS 
    			&& nextHoplastIPAddress >= Constants.MIN_VALID_NODE_ADDRESS
    			&& precursorNodes != null){
	    	this.nextHop = nextHopAddress;
	        for(String node : precursorNodes){
	        	addPrecursorAddress(node);
	        }
	        resetAliveTimeLeft();
	   	} else {
	   		throw new RouteNotValidException("RouteEntry: invalid parameters given");
	   	}
    }
    
    /**
     * Adds node as a precursor, so a RRER can be sent to this node in case of route failure
     * @param nodeAddress the address of the node which is using this forward route 
     * @return 
     */
    public boolean addPrecursorAddress(String nodeAddress){
        synchronized (precursorNodes) {
        	int nodelastIPAddress = Integer.parseInt(nodeAddress.split("\\.")[3]);
        	if(!precursorNodes.contains(nodeAddress)
        			&& nodelastIPAddress <= Constants.MAX_VALID_NODE_ADDRESS
        			&& nodelastIPAddress >= Constants.MIN_VALID_NODE_ADDRESS){
        		precursorNodes.add(nodeAddress);
        		return true;
        	}
        	return false;
		}
    }
    
    public ArrayList<String> getPrecursors(){
    	ArrayList<String> copy = new ArrayList<String>();
    	synchronized (precursorNodes) {
    		for(String address: precursorNodes){
    			copy.add(address);
    		}
		}
    	return copy;
    }
    
    public void resetAliveTimeLeft(){
    	synchronized (aliveTimeLock) {
    		alivetimeLeft = Constants.ROUTE_ALIVETIME + System.currentTimeMillis();	
		}
    }
    
    /**
     * @return returns true if this route is allowed to be used for packet forwarding.
     */
    public boolean isValid(){
    	return isValid;
    }
    
    public void setValid(boolean valid){
    	if(isValid != valid){
    		Debug.print("Forward Entry: isValid has changed to: "+valid);	
    	}
    	isValid = valid;
    }
    
    public boolean setSeqNum(int newSeqNr){
    	if(newSeqNr >= Constants.FIRST_SEQUENCE_NUMBER && newSeqNr <= Constants.MAX_SEQUENCE_NUMBER){
    		destSeqNum = newSeqNr;
    		return true;
    	}
    	return false;
    }
    
    public String getNextHop(){
    	return nextHop;
    }
}
