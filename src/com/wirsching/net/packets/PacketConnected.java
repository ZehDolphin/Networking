package com.wirsching.net.packets;

/**
 * The 'PacketConnected' packet is sent by the client once connected to the server. <br>
 * 
 * @author zehdolphin
 * @since 2017-11-30
 */
public class PacketConnected extends Packet {

	public PacketConnected() {
		id = "packet_connected";
	}

}
