package com.restaurantfx.network;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.function.Consumer;

public class ServerConnector {
	private int clientCount = 0;
	private final int port;
	private Consumer<Serializable> onRecieveCallback;
	private ConnectionThread connThread = new ConnectionThread();
	private Hashtable<String, ClientHandler> clientTable = new Hashtable<>();

	public ServerConnector(int port, Consumer<Serializable> onRecieveCallback) {
		this.port = port;
		this.onRecieveCallback = onRecieveCallback;
	}

	public void start() {
		connThread.start();
	}

	public void send(Serializable data, String clientID) throws Exception {
		ClientHandler clientHandle = clientTable.get(clientID);
		clientHandle.out.writeObject(data);
	}

	public void closeConnetion() throws Exception {
		try {
			connThread.serverSocket.close();
			connThread.clientHandle.socket.close();
		} catch (Exception e) {
		}
	}

	private class ConnectionThread extends Thread {
		ClientHandler clientHandle;
		ServerSocket serverSocket;

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(port);
				while (true) {
					Socket socket = serverSocket.accept();
					String session = getSession();
					clientHandle = new ClientHandler(socket, onRecieveCallback, session);
					clientTable.put(session, clientHandle);
					clientHandle.start();
				}
			} catch (Exception e) {
			}
		}
	}

	public int getPort() {
		return port;
	}

	public String getSession() {
		return "CLIENT_" + (++clientCount);
	}
}
