package com.wirsching.net.packets;

/**
 * The 'PacketDisconnected' packet is sent by the client when it disconnects from the server. <br>
 * 
 * @author zehdolphin
 * @since 2017-12-30
 */
public class PacketDisconnected extends Packet {

	public PacketDisconnected() {
		id = "packet_disconnected";
	}

}
