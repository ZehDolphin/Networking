package com.wirsching.net.client;

import com.wirsching.net.packets.Packet;

public interface ConnectionHandler {

	/**
	 * This method gets called once the client is successfully connected to a server. <br>
	 */
	public void connected(Packet packet);

	/**
	 * This method is called every time the client receives a packet from the server. <br>
	 */
	public void received(Packet packet);
	
	/**
	 * This method gets called once the client has been disconnected from the server. <br>
	 */
	public void disconnected();
	
	public void error(Packet packet);
	
}
