package main;

import com.wirsching.net.client.ConnectionHandler;
import com.wirsching.net.packets.Packet;
import com.wirsching.net.server.Server;

public class TestServer {

	public static void main(String[] args) {
		System.out.println("[SERVER] Starting server.");
		
		// Creating the server. Listen on '4321'
		Server server = new Server(4321).setConnectionHandler(new ConnectionHandler() {
			
			@Override
			public void connected(Packet packet) {
				System.out.println("[SERVER] " + packet.getIP() + ":" + packet.getPort() + " connected.");
			}
			
			@Override
			public void received(Packet packet) {
				
				
				
			}
			
			@Override
			public void disconnected(Packet packet) {
				
			}
			
			@Override
			public void error(Packet packet) {
			}
			
		});
		
		// Start the server.
		server.start();
		
	}
	
}
