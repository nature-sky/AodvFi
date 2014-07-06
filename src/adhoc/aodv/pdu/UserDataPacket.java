package adhoc.aodv.pdu;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.BadPduFormatException;

public class UserDataPacket implements Packet{
	private byte[] data;
	private String destIPAddress;
	private byte pduType;
	private String srcIPAddress;
	private String packetID;
	
	public UserDataPacket(){
		
	}
	
	public UserDataPacket(String packetIdentifier,String destIPAddress, byte[] data, String srcIPAddress){
		pduType = Constants.USER_DATA_PACKET_PDU;
		packetID = packetIdentifier;
		this.destIPAddress = destIPAddress;
		this.data = data;
		this.srcIPAddress = srcIPAddress;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public String getSourceNodeAddress(){
		return srcIPAddress;
	}
	
	@Override
    public String getDestinationAddress() {
        return destIPAddress;
    }
	
	@Override
	public byte[] toBytes() {
		return toString().getBytes();
	}

	@Override
	public String toString(){
		return pduType+";"+srcIPAddress+";"+destIPAddress+";"+new String(data);
	}
	
	@Override
	public void parseBytes(byte[] rawPdu) throws BadPduFormatException {
		String[] s = new String(rawPdu).split(";",4);
		if(s.length != 4){
			throw new BadPduFormatException(	"UserDataPacket: could not split " +
												"the expected # of arguments from rawPdu. " +
												"Expecteded 4 args but were given "+s.length	);
		}
		try {
			pduType = Byte.parseByte(s[0]);
			if(pduType != Constants.USER_DATA_PACKET_PDU){
				throw new BadPduFormatException(	"UserDataPacket: pdu type did not match. " +
													"Was expecting: "+Constants.USER_DATA_PACKET_PDU+
													" but parsed: "+pduType	);
			}
			srcIPAddress = s[1].toString();
			destIPAddress = s[2].toString();
			data = s[3].getBytes();
		} catch (NumberFormatException e) {
			throw new BadPduFormatException(	"UserDataPacket: falied in parsing " +
												"arguments to the desired types"	);
		}
	}

	public String getPacketID() {
		return packetID;
	}

}