package com.wirsching.net.client;

import com.wirsching.net.packets.Packet;

/**
 * The ConnectionHandler is an Interface to structure the incoming data from the
 * Server or Client. <br>
 * 
 * @author DebugDrive
 * @since 2017-11-28
 *
 */
public interface ConnectionHandler {

	/**
	 * This method gets called once the client is successfully connected to a
	 * server. <br>
	 * 
	 * @param packet
	 *            - The received packet associated with the connection.
	 */
	public void connected(Packet packet);

	/**
	 * This method is called every time the client receives a packet from the
	 * server. <br>
	 * 
	 * @param packet
	 *            - The received packet.
	 */
	public void received(Packet packet);

	/**
	 * This method gets called once the client has been disconnected from the
	 * server. <br>
	 * 
	 * @param packet
	 *            - The received packet associated with the disconnection.
	 * 
	 * 
	 */
	public void disconnected(Packet packet);

	public void error(Packet packet);

}
