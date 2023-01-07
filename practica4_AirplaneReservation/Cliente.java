package practica4_AirplaneReservation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/*
 * Pre:---
 * Post: This class starts a client session, creates in and out channels, 
 * 
 */
public class Cliente extends Conexion {
	protected String clientName;
    public Cliente(String clientName) throws IOException {
    	super("cliente");
    	this.clientName = clientName;
    	
    } //Se usa el constructor para cliente de Conexion

    public void startClient() {//Método para iniciar el cliente
        try {
        	// Canal para recibir mensajes (entrada)
        	System.out.println("Client session start");
        	DataInputStream in = new DataInputStream(cs.getInputStream());
        	// Canal para enviar mensajes (salida)
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            char[] filas = "1234".toCharArray();
            char[] sillas = "ABCD".toCharArray();
            while(true) {
            	for(char fila : filas) {
            		for(char silla: sillas) {
            			
            		}
            	}
            	System.out.println("Escribe una frase (END OF SERVICE para terminar): ");
            	String cadena = entrada.nextLine();
            	out.writeUTF(cadena);
            	if(cadena.equalsIgnoreCase("END OF SERVICE")) break;
            	System.out.println(in.readUTF());
            }
            cs.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
