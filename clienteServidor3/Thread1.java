package clienteServidor3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Pre: ---
 * Post: Esta clase implementa un hilo que ejecuta un código que maneja un
 * socket, configura canales de entrada y salida, y se comunica con un cliente
 * para recibir mensajes y enviar respuestas
 */
public class Thread1 extends Thread{
	protected Socket cs;
	ArrayList<Thread1> clientes;

	public Thread1(Socket cs, ArrayList<Thread1> clientes) throws IOException {
		super();
		this.cs = cs;
		this.clientes = clientes;
	}
	
	@Override
    public void run() {
		try {
			System.out.println("Cliente en línea");
			
			DataInputStream in = new DataInputStream(cs.getInputStream());
	        DataOutputStream out = new DataOutputStream(cs.getOutputStream());
	        out.writeUTF("Petición recibida y aceptada");
            while(true) {
	            String mensaje = in.readUTF();
	            if(mensaje.equalsIgnoreCase("END OF SERVICE")) break;
	            System.out.println("Mensaje recibido -> " + mensaje);
	            //System.out.println("Vocales en: \"" + mensaje + "\" : " + calcularVocales(mensaje));
	            out.writeUTF("Vocales en: \"" + mensaje + "\" : " + calcularVocales(mensaje));
            }
            System.out.println("Servicio terminado");
            cs.close();//Se finaliza la conexión con el cliente
            clientes.remove(this);
		}catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	private int calcularVocales(String cadena) {
    	int vocales = 0;
    	for (int x = 0; x < cadena.length(); x++) {
    	    char letraActual = cadena.charAt(x);
    	    if (esVocal(letraActual)) vocales++;
    	}
    	return vocales;
    }
    
    private static boolean esVocal(char letra) {
        return "aeiou".contains(String.valueOf(letra).toLowerCase());
    }
	
}
