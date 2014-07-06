package adhoc.aodv.pdu;



public abstract class AodvPDU implements Packet{
	protected byte pduType;
    //protected int srcAddress, destAddress;
    protected String srcIPAddress;
    protected String destIPAddress;
    protected int destSeqNum;
    
    
    public AodvPDU(){
    	
    }
    
    public AodvPDU(String srcIPAddress, String destIPAddress, int destinationSequenceNumber){
        this.srcIPAddress = srcIPAddress;
    	this.destIPAddress = destIPAddress;
    	destSeqNum = destinationSequenceNumber;
    }
    
    public String getSourceAddress() {
        return srcIPAddress;
    }

    @Override
    public String getDestinationAddress() {
        return destIPAddress;
    }

    public int getDestinationSequenceNumber() {
        return destSeqNum;
    }
    
    public byte getType(){
    	return pduType;
    }
    
    @Override
    public String toString(){
    	return Byte.toString(pduType)+";"+srcIPAddress+";"+destIPAddress+";"+destSeqNum+";";
    }
}
