package com.wirsching.net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.wirsching.net.packets.Packet;
import com.wirsching.net.packets.PacketConnected;
import com.wirsching.net.packets.PacketServerError;

/**
 * The Client is used to connect to a Server. <br>
 * 
 * @author zehdolphin
 * @since 2017-11-28
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
	 * The client socket. <br>
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
	 * This is the ConnectionHandler. <br>
	 */
	public ConnectionHandler connectionHandler;
	
	public Client() {
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects the client to a server using a valid IP and port. <br>
	 * 
	 * @param ip - The target IP address.
	 * @param port - The target port number.
	 */
	public Client connectToServer(String ip, int port) {
		try {
			this.ipAddress = InetAddress.getByName(ip);
			this.port = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		start();
		sendPacket(new PacketConnected());
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
			
			byte[] data = new byte[packetSize];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String m = new String(packet.getData()).trim();
			
			Packet p = Packet.parse(m);
			
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
	
	public void sendPacket(Packet packet) {
		sendString(packet.toString());
	}
	
	public void sendString(String data) {
		sendData(data.getBytes());
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {

	}
	

}
