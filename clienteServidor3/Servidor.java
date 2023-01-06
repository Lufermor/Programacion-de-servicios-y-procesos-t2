package clienteServidor3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Servidor extends Conexion { //Se hereda de conexión para hacer uso de los sockets y demás
	
    public Servidor() throws IOException {
    	super("servidor");
    }

    public void startServer() {//Método para iniciar el servidor
        try {
            System.out.println("Esperando..."); //Esperando conexión
            cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
            System.out.println("Cliente en línea");

            DataInputStream in = new DataInputStream(cs.getInputStream());
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            
            //Se le envía un mensaje al cliente usando su flujo de salida
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
        }
        catch (Exception e) {
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
