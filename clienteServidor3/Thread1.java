package clienteServidor3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * Pre: ---
 * Post: Esta clase implementa un hilo que ejecuta un código que maneja un
 * socket, configura canales de entrada y salida, y se comunica con un cliente
 * para recibir mensajes y enviar respuestas
 */
public class Thread1 extends Thread{
	protected Socket cs;

	public Thread1(Socket cs) throws IOException {
		super();
		this.cs = cs;
	}
	
	@Override
    public void run() {
		try {
			DataInputStream in = new DataInputStream(cs.getInputStream());
	        DataOutputStream out = new DataOutputStream(cs.getOutputStream());
		}catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
	
}
