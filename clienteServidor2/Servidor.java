package clienteServidor2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Servidor extends Conexion { //Se hereda de conexión para hacer uso de los sockets y demás
	
    public Servidor() throws IOException {
    	super("servidor");
    }

    public void startServer() {//Método para iniciar el servidor
        try {
        	while(true) {
	            System.out.println("Esperando..."); //Esperando conexión
	            cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
	            System.out.println("Cliente en línea");
	
	            DataInputStream in = new DataInputStream(cs.getInputStream());
	            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
                
	            //Se le envía un mensaje al cliente usando su flujo de salida
	            out.writeUTF("Petición recibida y aceptada");
	            String mensaje = in.readUTF();
	            System.out.println("Mensaje recibido -> " + mensaje);
	           
	            System.out.println("Fin de la conexión");
	            cs.close();//Se finaliza la conexión con el cliente
        	}
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}