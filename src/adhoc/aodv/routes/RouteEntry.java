package adhoc.aodv.routes;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.RouteNotValidException;

public abstract class RouteEntry {
    protected String destAddress;
    protected volatile long alivetimeLeft;
    protected volatile int destSeqNum;
    protected int hopCount;
    protected final Object aliveTimeLock = new Integer(0);
    
    public RouteEntry(int hopCount, int destSeqNum, String destAddress) throws RouteNotValidException{
    	System.out.println("destAddress:"+destAddress);
    	int destlastIPAddress = Integer.parseInt(destAddress.split("\\.")[3]);
    	if(destlastIPAddress <= Constants.MAX_VALID_NODE_ADDRESS && destlastIPAddress >= Constants.MIN_VALID_NODE_ADDRESS
        		&& (destSeqNum <= Constants.MAX_SEQUENCE_NUMBER 
        				&& destSeqNum >= Constants.FIRST_SEQUENCE_NUMBER 
        				|| destSeqNum == Constants.UNKNOWN_SEQUENCE_NUMBER)){
	    	this.hopCount = hopCount;
	    	this.destSeqNum = destSeqNum;
	    	this.destAddress = destAddress;
    	} else {
    		throw new RouteNotValidException("RouteEntry: invalid parameters given");
    	}
    }
    
    /**
     * 
     * @return the system time of when the route becomes stale
     */
    public long getAliveTimeLeft(){
    	synchronized (aliveTimeLock) {
    		return alivetimeLeft;
    	}
    }
    
    public abstract void resetAliveTimeLeft();
    
    public int getDestinationSequenceNumber(){
        return destSeqNum;	
    }
    
    public int getHopCount(){
    	return hopCount;
    }
    
    public String getDestinationAddress() {
        return destAddress;
    }
    
}
