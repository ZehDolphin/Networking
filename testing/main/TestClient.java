package main;

import com.wirsching.net.client.Client;
import com.wirsching.net.client.ConnectionHandler;
import com.wirsching.net.packets.Packet;

import main.packets.ChatMessage;

public class TestClient {

	public static void main(String[] args) {
		System.out.println("[CLIENT] Starting client.");

		// Create the client and connect it to the server. 
		Client client = new Client();
		
		client.addConnectionHandler(new ConnectionHandler() {
			
			@Override
			public void connected(Packet packet) {
				client.sendString("ping");
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
				
		}).connectToServer("localhost", 4321);
	
		
		client.sendPacket(new ChatMessage("Hello World!"));
		
		
		
	}
	
}
