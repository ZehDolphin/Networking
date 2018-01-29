package com.wirsching.net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.wirsching.net.packets.Packet;
import com.wirsching.net.packets.PacketConnected;
import com.wirsching.net.packets.PacketDisconnected;

/**
 * The <i>Client</i> is used to connect to a <i>Server</i>. <br>
 * It uses a DatagramSocket to create the connection and then <br>
 * embeds a simple packet system for data organization. <br>
 * 
 * @author ZehDolphin
 * @since 2017-11-28
 * @see java.net.DatagramSocket
 * @see java.net.InetAddress
 */
public class Client extends Thread {

	/**
	 * The InetAddress to the Server. <br>
	 */
	private InetAddress ipAddress;
	
	/**
	 * The port to the Server. <br>
	 */
	private int port;

	/**
	 * The Client socket. <br>
	 */
	private DatagramSocket socket;
	
	/**
	 * The maximum packet size that can be received by the client. <br>
	 * This number is arbitrary and can be changed later. <br>
	 * Though it should be noted that every time the client waits for a response it will <br>
	 * allocate this amount of memory! <br>
	 */
	private int packetSize = 1024;
	
	/**
	 * This is the clients instance of the ConnectionHandler. <br>
	 */
	public ConnectionHandler connectionHandler;
	
	public Client() {
		try {
			
			// Create a new DatagramSocket.
			this.socket = new DatagramSocket();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects the Client to a Server using a valid IP and port. <br>
	 * 
	 * @param ip - The target IP address.
	 * @param port - The target port number.
	 */
	public Client connectToServer(String ip, int port, Packet... packet) {
		try {
			this.ipAddress = InetAddress.getByName(ip);
			this.port = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		start();
		if (packet.length == 0)
			sendPacket(new PacketConnected());
		else
			sendPacket(packet[0]);
		connectionHandler.connected(null);
		return this;
	}
	
	/**
	 * Set the ConnectionHandler.
	 */
	public Client addConnectionHandler(ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		return this;
	}
	
	public void run() {
		while (true) {
			
			// Allocate memory for the incoming packet of data.
			byte[] data = new byte[packetSize];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			
			try {
				
				// Wait until data is received from the server.
				socket.receive(packet);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Parse and trim the incoming data.
			String m = new String(packet.getData()).trim();
			
			// Create a Packet from the string of data.
			Packet p = Packet.parse(m);
			
			// Check for "critical" Packets.
			switch (p.getID()) { 
			
			case "server_error":
				connectionHandler.error(p);
				break;
			
			default:
				connectionHandler.received(p);
				break;
			
			}
						
		}
	}
	
	/**
	 * Sends a Packet of data.
	 * 
	 * @param packet - The packet to send.
	 */
	public void sendPacket(Packet packet) {
		sendString(packet.toString());
	}
	
	/**
	 * Sends a String of data. <br>
	 * NOTE! This is not recommended as the Client-Server system already has a Packet system. <br>
	 * @param data - The String of data to send.
	 */
	public void sendString(String data) {
		sendData(data.getBytes());
	}
	
	/**
	 * Sends a byte array of data to the Server. <br>
	 * 
	 * @param data - The array of bytes to send.
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(Packet... packet) {
		if (packet.length == 0)
			sendPacket(new PacketDisconnected());
		else
			sendPacket(packet[0]);
		connectionHandler.disconnected(null);
	}
	

}
