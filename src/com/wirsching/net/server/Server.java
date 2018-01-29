package com.wirsching.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.wirsching.net.client.ConnectionHandler;
import com.wirsching.net.packets.Packet;
import com.wirsching.net.packets.PacketServerError;

public class Server extends Thread {

	private DatagramSocket socket;
	
	private int port;
	
	private int packetSize = 1024;
	
	
	/**
	 * This is the ConnectionHandler. <br>
	 */
	public ConnectionHandler connectionHandler;
	
	public Server(int port) {
		this.port = port;
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	
	/**
	 * Set the ConnectionHandler.
	 */
	public Server setConnectionHandler(ConnectionHandler connectionHandler) {
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
			
			p.setIP(packet.getAddress().getHostAddress());
			p.setPort(packet.getPort());
			
			
			if (p.getID() == "SERVER_ERROR") {

				System.err.println("[SERVER] Received unknown message from client: '" + p.getIP() + ":" + p.getPort() + "'!");
				
				// Handle the error server side.
				connectionHandler.error(p);

				PacketServerError e = (PacketServerError) new PacketServerError().sender(packet.getAddress().getHostAddress(), packet.getPort());
				e.putData("error", "unknown_message");
				e.putData("original_message", m);
				sendPacket(e);

				
				continue;
			}
			
			
			switch (p.getID()) { 
			
			case Packet.PACKET_CONNECT:
				connectionHandler.connected(p);
				break;
				
			case Packet.PACKET_DISCONNECT:
				connectionHandler.disconnected(p);
				break;
			
			default:
				connectionHandler.received(p);
				break;
			
			}
			
			
			
			
			
		}
	}
	
	public void sendPacket(Packet packet) {
		sendPacket(packet, packet.getIP(), packet.getPort());
	}
	
	public void sendPacket(Packet packet, String ip, int port) {
		try {
			sendString(packet.toString(), InetAddress.getByName(ip), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void sendString(String data, InetAddress ipAddress, int port) {
		sendData(data.getBytes(), ipAddress, port);
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
