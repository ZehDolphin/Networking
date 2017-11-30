package main.packets;

import com.wirsching.net.packets.Packet;

public class ChatMessage extends Packet {

	public ChatMessage(String message) {
		id = "chat_message";
		putData("message", message);
	}

}
