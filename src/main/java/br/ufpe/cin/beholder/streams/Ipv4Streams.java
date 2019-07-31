package br.ufpe.cin.beholder.streams;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.IcmpV4Type;

import com.espertech.esper.client.EPRuntime;

import br.ufpe.cin.beholder.packets.Ipv4PacketSender;

public class Ipv4Streams extends Thread {

	private EPRuntime cepLocal;

	public Ipv4Streams(EPRuntime cepLocal) {
		this.cepLocal = cepLocal;
	}

	public void run() {
		// int icmpv4Count = 0;
		// int echoCount = 0;

		while (true) {
			try {
				//KURA IP ADDRESS
				InetAddress addr = InetAddress.getByName("172.16.1.1");
				PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

				int snapLen = 65536;
				PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
				int timeout = 10;
				PcapHandle handle = nif.openLive(snapLen, mode, timeout);

				Packet packet = handle.getNextPacketEx();
				handle.close();

				IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
				try {

					Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
					Inet4Address dstAddr = ipV4Packet.getHeader().getDstAddr();
					//short length = ipV4Packet.getHeader().getTotalLength();

					IcmpV4Type echo = new IcmpV4Type((byte) 8, "Echo");

					Thread.sleep(25);
					this.cepLocal.sendEvent(new Ipv4PacketSender(srcAddr.toString(), dstAddr.toString(), echo));
					System.out.println(packet);

				} catch (Exception e) {
					// TODO: handle exception

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}