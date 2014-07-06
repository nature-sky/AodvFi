package adhoc.aodv.pdu;

import java.util.ArrayList;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.BadPduFormatException;

public class RERR extends AodvPDU {
	private String unreachableNodeAddress;
	private int unreachableNodeSequenceNumber;
	private ArrayList<String> destAddresses = new ArrayList<String>();

	
	
	public RERR(){
		
	}
	
	/**
	 * 
	 * @param unreachableNodeAddress
	 * @param unreachableNodeSequenceNumber
	 * @param destinationAddresses
	 */
    public RERR(String unreachableNodeAddress ,int unreachableNodeSequenceNumber, ArrayList<String> destinationAddresses) {
    	this.unreachableNodeAddress = unreachableNodeAddress;
    	this.unreachableNodeSequenceNumber = unreachableNodeSequenceNumber;
    	pduType = Constants.RERR_PDU;
        destAddresses = destinationAddresses;
        destIPAddress = "Dest IP ERROR";
    }

	/**
	 * Constructor of a route error message  
	 * @param
	 * @param
	 * @param destinationAddress the node which hopefully will receive this PDU packet
	 */
    public RERR(String unreachableNodeAddress ,int unreachableNodeSequenceNumber, String destinationAddress){
    	this.unreachableNodeAddress = unreachableNodeAddress;
    	this.unreachableNodeSequenceNumber = unreachableNodeSequenceNumber;
    	pduType = Constants.RERR_PDU;
        destIPAddress = destinationAddress;
    }
    
	public String getUnreachableNodeAddress(){
		return unreachableNodeAddress;
	}
	
	public int getUnreachableNodeSequenceNumber(){
		return unreachableNodeSequenceNumber;
	}
	
	public ArrayList<String> getAllDestAddresses(){
		return destAddresses;
	}
	
	@Override
	public byte[] toBytes() {
		return this.toString().getBytes();
	}
	
	@Override
	public String toString() {
		return Byte.toString(pduType)+";"+unreachableNodeAddress+";"+unreachableNodeSequenceNumber;
	}
	
	@Override
	public void parseBytes(byte[] rawPdu) throws BadPduFormatException {
		String[] s = new String(rawPdu).split(";",3);
		if(s.length != 3){
			throw new BadPduFormatException(	"RERR: could not split " +
												"the expected # of arguments from rawPdu. " +
												"Expecteded 3 args but were given "+s.length	);
		}
		try {
			pduType = Byte.parseByte(s[0]);
			if(pduType != Constants.RERR_PDU){
				throw new BadPduFormatException(	"RERR: pdu type did not match. " +
													"Was expecting: "+Constants.RERR_PDU+
													" but parsed: "+pduType	);
			}
			unreachableNodeAddress = s[1].toString();
			unreachableNodeSequenceNumber = Integer.parseInt(s[2]);
		} catch (NumberFormatException e) {
			throw new BadPduFormatException("RERR: falied in parsing arguments to the desired types");
		}	
	}
}
