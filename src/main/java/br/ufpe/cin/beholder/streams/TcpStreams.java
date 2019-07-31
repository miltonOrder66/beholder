package br.ufpe.cin.beholder.streams;

import java.net.Inet4Address;
//import java.net.Inet6Address;
import java.net.InetAddress;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
//import org.pcap4j.packet.UdpPacket;

import com.espertech.esper.client.EPRuntime;

import br.ufpe.cin.beholder.packets.TcpPacketSender;

public class TcpStreams extends Thread {

	private EPRuntime cepLocal;
	private String evilHost;

	public TcpStreams(EPRuntime cepLocal) {
		this.cepLocal = cepLocal;
	}

	public void run() {
		int synCount = 0;

		while (true) {
			try {
				InetAddress addr = InetAddress.getByName("172.16.1.1");
				PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

				int snapLen = 65536;
				PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
				int timeout = 1;
				PcapHandle handle = nif.openLive(snapLen, mode, timeout);

				Packet packet = handle.getNextPacketEx();
				handle.close();

				IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
				// IpV6Packet ipV6Packet = packet.get(IpV6Packet.class);
				TcpPacket tcpPacket = packet.get(TcpPacket.class);

				try {

					Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
					Inet4Address dstAddr = ipV4Packet.getHeader().getDstAddr();
					// short length = ipV4Packet.getHeader().getTotalLength();
					// int tcpLength = tcpPacket.getHeader()length();
					boolean syn = tcpPacket.getHeader().getSyn();
					boolean ack = tcpPacket.getHeader().getAck();
					evilHost = srcAddr.getHostAddress();

					if (syn = true) {
						synCount++;
					} else
						synCount = 0;

					Thread.sleep(25);
					this.cepLocal.sendEvent(
							new TcpPacketSender(srcAddr.toString(), dstAddr.toString(), syn, ack, synCount, evilHost));

					System.out.println(srcAddr + " / " + dstAddr);
					// System.out.println(packet);

				} catch (Exception e) {
					// TODO: handle exception

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
