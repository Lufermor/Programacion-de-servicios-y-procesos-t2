package practica4_AirplaneReservation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Pre:---
 * Post: This class starts a client session, creates in and out channels, 
 * Communicates with the server to start booking seats, until the flight is full
 * finally it notifies the seats succesfully booked. 
 */
public class Cliente extends Conexion {
	protected String clientName;
	protected String bookedseats;
    public Cliente(String clientName) throws IOException {
    	super("cliente");
    	this.clientName = clientName;
    	bookedseats = "";
    } //Se usa el constructor para cliente de Conexion

    /*
     * Pre: ---
     * Post: Creates in and out channels, notifies the server to start booking, 
     * when a seat is reserved or already busy, the method tries to book another seat
     * until it receives the response "FLIGHT FULL".
     */
    public void startClient() {//Método para iniciar el cliente
        try {
        	// Canal para recibir mensajes (entrada)
        	System.out.println("Client session start");
        	DataInputStream in = new DataInputStream(cs.getInputStream());
        	// Canal para enviar mensajes (salida)
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            String mensaje = in.readUTF();
            System.out.println(mensaje);
//            char[] filas = "1234".toCharArray();
//            char[] sillas = "ABCD".toCharArray();
            String request = "BOOK:1A";
            out.writeUTF("START BUY:" + clientName);
            while(true) {
            	out.writeUTF(request);
            	System.out.println("Booking seat: " + request.split(":")[1]);
            	mensaje = in.readUTF();
            	System.out.println(mensaje);
            	if(mensaje.equalsIgnoreCase("FLIGHT FULL")) break;
	            if(mensaje.split(":")[0].equalsIgnoreCase("RESERVED")) {
	            	bookedseats += mensaje.split(":")[1] + " ";
	            	int filaReservada = Integer.parseInt("" + request.split(":")[1].charAt(0));
	            	if(filaReservada == 4) {
	            		request = "BOOK:" + 3 + request.split(":")[1].charAt(1);
	            	}else request = "BOOK:" + (filaReservada + 1) + request.split(":")[1].charAt(1);
	            }else if(mensaje.split(":")[0].equalsIgnoreCase("SEAT BUSY")) {
	            	String sillas[] = mensaje.split(":")[1].split("-");
	            	for(int i = 0; i < sillas.length; i++) {
	            		if(sillas[i].contains("L")) {
	            			char silla = "ABCD".charAt(sillas[i].indexOf("L"));
	            			request = "BOOK:" + (i+1) + silla;
	            		}
	            	}
	            }
	            
//            	for(char fila : filas) {
//            		for(char silla: sillas) {
//            			out.writeUTF("BOOK:" + fila + silla);
//            			mensaje = in.readUTF();
//        	            if(mensaje.equalsIgnoreCase("FLIGHT FULL")) break;
//        	            if(mensaje.split(":")[0].equalsIgnoreCase("RESERVED")) {
//        	            	bookedseats += mensaje.split(":")[1] + " ";
//        	            }
//            		}
//            	}
            }
            System.out.println(clientName + " Session finished");
            System.out.println(clientName + " Boookes seats: " + bookedseats);
            cs.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
