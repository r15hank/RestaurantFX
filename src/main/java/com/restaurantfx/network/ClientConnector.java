package com.restaurantfx.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnector {
	private String clientID;
	private final String ipAddress;
	private final int port;
	private Consumer<Serializable> onRecieveCallback;
	private ConnectionThread connThread = new ConnectionThread();

	public ClientConnector(String ipAddress, int port, Consumer<Serializable> onRecieveCallback) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.onRecieveCallback = onRecieveCallback;
	}

	public void start() {
		connThread.start();
	}

	public void send(Serializable data) throws Exception {
		connThread.out.writeObject(data);
	}

	public void closeConnetion() throws Exception {
		try {
			connThread.socket.close();
		} catch (Exception e) {
		}
	}

	private class ConnectionThread extends Thread {
		Socket socket;
		ObjectOutputStream out;

		@Override
		public void run() {
			try {
				Socket socket = new Socket(ipAddress, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

				this.socket = socket;
				this.out = out;
				socket.setTcpNoDelay(true);
				clientID = in.readUTF();
				while (true) {
					Serializable data = (Serializable) in.readObject();
					onRecieveCallback.accept(data);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public String getClientID() {
		return clientID;
	}
}
