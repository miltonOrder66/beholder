package br.ufpe.cin.beholder.application;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import br.ufpe.cin.beholder.messages.SynFloodListener;
import br.ufpe.cin.beholder.packets.TcpPacketSender;
import br.ufpe.cin.beholder.streams.TcpStreams;

public class MainCEP {

	
	public static void main(String[] args) {
		try {
			Configuration config = new Configuration();
			config.addEventType("TCP_Stream", TcpPacketSender.class);
			// config.addEventType("UDP_Stream", UdpPacketSender.class);
			// config.addEventType("ICMPv4_Stream", Ipv4PacketSender.class);
			// config.addEventType("Land_Stream", TcpPacketSender.class);

			EPServiceProvider tcpCep = EPServiceProviderManager.getProvider("esper-tcp", config);
			// EPServiceProvider udpCep = EPServiceProviderManager.getProvider("esper- udp",
			// config);
			// EPServiceProvider icmpCep =
			// EPServiceProviderManager.getProvider("esper-icmpv4", config);
			// EPServiceProvider landCep =
			// EPServiceProviderManager.getProvider("esper-land", config);

			// ******************DoS Attacks!***************************

			// String evilHost = null;
			// ---------SYN FLOOD
			EPStatement synFloodStatement = tcpCep.getEPAdministrator()
					.createEPL("INSERT INTO evilHost SELECT srcAddr FROM TCP_Stream WHERE srcAddr ='/172.16.1.102'");
			synFloodStatement.addListener(new SynFloodListener());
//
			// s = s.substring(1);

			/*
			 * syn flood EPStatement synFloodStatement = tcpCep.getEPAdministrator().
			 * createEPL("INSERT INTO badHost SELECT srcAddr FROM TCP_Stream.win:time(1 sec) HAVING COUNT(syn) >8 AND NOT(ack)"
			 * ); synFloodStatement.addListener(new SynFloodListener()); BufferedWriter
			 * fileGuardian = new BufferedWriter( new
			 * FileWriter("/var/log/beholder/alert")); fileGuardian.newLine();
			 * fileGuardian.write(badhost); fileGuardian.close();
			 */

			/*
			 * ----------UDP Flood EPStatement udpFloodStatement =
			 * udpCep.getEPAdministrator().createEPL("select count(udpPacket), srcAddr " +
			 * "from UDP_Stream.win:time(1 sec) having count(udpPacket) > 8");
			 * udpFloodStatement.addListener(new UdpFloodListener());
			 * 
			 * ----------ICMP flood EPStatement icmpFloodStatement =
			 * icmpCep.getEPAdministrator().createEPL(
			 * "select count(echo) as echoCount , srcAddr, dstAddr from ICMPv4_Stream.win:time(1 sec) having count(echo) > 50 "
			 * ); icmpFloodStatement.addListener(new IcmpV4FloodListener());
			 * 
			 * EPStatement synFloodStatement =
			 * tcpCep.getEPAdministrator().createEPL("select count(syn), srcAddr, dstAddr "
			 * + "from TCP_Stream.win:time(1 sec) having count(syn) >8 and not(ack)");
			 * synFloodStatement.addListener(new SynFloodListener()); // ----------UDP Flood
			 * //EPStatement udpFloodStatement =
			 * udpCep.getEPAdministrator().createEPL("select count(udpPacket), srcAddr " //
			 * + "from UDP_Stream.win:time(1 sec) having count(udpPacket) > 8");
			 * //udpFloodStatement.addListener(new UdpFloodListener());
			 * 
			 * // ----------ICMP flood //EPStatement icmpFloodStatement =
			 * icmpCep.getEPAdministrator().createEPL( //
			 * "select count(echo) as echoCount , srcAddr, dstAddr from ICMPv4_Stream.win:time(1 sec) having count(echo) > 50 "
			 * ); //icmpFloodStatement.addListener(new IcmpV4FloodListener());
			 * 
			 * // ******************DDoS Attacks!*************************** // In
			 * progress.....
			 */

			new TcpStreams(tcpCep.getEPRuntime()).start();
			// new UdpStreams(udpCep.getEPRuntime()).start();
			// new Ipv4Streams(icmpCep.getEPRuntime()).start();

			System.out.println("********************************Starting Beholder**********************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
