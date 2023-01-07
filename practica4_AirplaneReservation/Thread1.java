package practica4_AirplaneReservation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Pre: ---
 * Post: This class implements a thread that executes code that handles a
 * socket, configures input and output channels, and communicates with a client
 * to receive messages and send replies
 */
public class Thread1 extends Thread{
	protected Socket cs;
	ArrayList<Thread1> clientes;
	AirplaneSeats vuelo;
	Servidor servidor;

	public Thread1(Socket cs, ArrayList<Thread1> clientes, AirplaneSeats vuelo, Servidor servidor) throws IOException {
		super();
		this.cs = cs;
		this.clientes = clientes;
		this.vuelo = vuelo;
		this.servidor = servidor;
	}

	/*
	 * Pre: ---
	 * Post: Create the input and output channels of the socket,
	 * send and receive messages with the client, when receiving message,
	 * calls the AirplaneSeats object to book a flight
	 * and returns a message to the client with the result.
	 */
	@Override
    public void run() {
		try {
			DataInputStream in = new DataInputStream(cs.getInputStream());
	        DataOutputStream out = new DataOutputStream(cs.getOutputStream());
	        out.writeUTF("Request received and accepted");
            while(true) {
	            String mensaje = in.readUTF();
	            if(mensaje.equalsIgnoreCase("END OF SERVICE")) break;
	            if(mensaje.split(":")[0].equalsIgnoreCase("START BUY")) {
	            	System.out.println("WELCOME TO SERVICE" + mensaje.split(":")[0]);
	            }else if(mensaje.split(":")[0] != "BOOK") {
	            	out.writeUTF("Invalid request");
	            	continue;
	            }
	            if(mensaje.split(":")[1].length() != 2) {
	            	out.writeUTF("Booked seat doesn't exist");
	            	continue;
	            }
	            String respuesta = vuelo.reserve(mensaje.split(":")[1]);
	            out.writeUTF(respuesta);
	            if(respuesta.equalsIgnoreCase("FLIGHT FULL")) {
	            	servidor.setFinalizar(true);
	            	break;
	            };
            }
            System.out.println("Client log out");
            cs.close();//Se finaliza la conexión con el cliente
            clientes.remove(this); //Con esta línea eliminamos este hilo del arrayList
            System.out.println("Clients online: " + clientes.size());
		}catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
}
