package br.ufpe.cin.beholder.packets;

public class TcpPacketSender {

	private String srcAddr;
	private String dstAddr;
	// private int packetLength;
	private boolean syn;
	private boolean ack;
	private int synCount;
	private String evilHost;
	// TcpPacket tcpPacket;

	public TcpPacketSender(String srcAddr, String dstAddr, boolean syn, boolean ack, int synCount, String evilHost) {
		this.srcAddr = srcAddr;
		this.dstAddr = dstAddr;
		// this.packetLength = packetLength;
		this.syn = syn;
		this.ack = ack;
		this.synCount = synCount;
		this.evilHost = evilHost;
	}

	public TcpPacketSender(String evilHost) {
		this.evilHost = evilHost;
	}

	public TcpPacketSender() {

	}

	public String getSrcAddr() {
		return srcAddr;
	}

	public String getDstAddr() {
		return dstAddr;
	}

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}

	public void setDstAddr(String dstAddr) {
		this.dstAddr = dstAddr;
	}

	public boolean isSyn() {
		return syn;
	}

	public boolean isAck() {
		return ack;
	}

	public int getSynCount() {
		return synCount;
	}

	public String getEvilHost() {
		return evilHost;
	}

	public void setEvilHost(String evilHost) {
		this.evilHost = evilHost;
	}

}
