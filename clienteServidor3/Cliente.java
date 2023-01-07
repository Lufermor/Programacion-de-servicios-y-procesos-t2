package clienteServidor3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Cliente extends Conexion {
    public Cliente() throws IOException {
    	super("cliente");
    } //Se usa el constructor para cliente de Conexion

    public void startClient() {//Método para iniciar el cliente
        try {
        	// Canal para recibir mensajes (entrada)
        	System.out.println("Cliente iniciado");
        	DataInputStream in = new DataInputStream(cs.getInputStream());
        	// Canal para enviar mensajes (salida)
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            System.out.println("Esperando mensaje inicial");
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            
            Scanner entrada = new Scanner(System.in);
            while(true) {
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
