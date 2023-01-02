package com.restaurantfx.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientHandler extends Thread {
	private Consumer<Serializable> onRecieveCallback;

	Socket socket;
	ObjectOutputStream out;
	String session;

	public ClientHandler(Socket socket, Consumer<Serializable> onRecieveCallback, String session) {
		this.socket = socket;
		this.onRecieveCallback = onRecieveCallback;
		this.session = session;
	}

	public void send(Serializable data) throws Exception {
		out.writeObject(data);
	}

	public void closeConnetion() throws Exception {
		try {
			socket.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			this.out = out;
			socket.setTcpNoDelay(true);
			out.writeUTF(session);
			out.flush();
			while (true) {
				Serializable data = (Serializable) in.readObject();
				onRecieveCallback.accept(data);
			}
		} catch (Exception e) {
		}

	}

}
