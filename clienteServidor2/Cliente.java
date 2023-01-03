package clienteServidor2;

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
        	System.out.println("a");
        	// Canal para recibir mensajes (entrada)
        	DataInputStream in = new DataInputStream(cs.getInputStream());System.out.println("a");
        	// Canal para enviar mensajes (salida)	
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());System.out.println("a");
            System.out.println("a");
            System.out.println(in.readUTF());
            System.out.println("b");
            String mensaje = in.readUTF();
            System.out.println("c");
            System.out.println(mensaje);
//            Scanner entrada = new Scanner(System.in);
            mensaje = "aaaaaaaaaaAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaaaaaa desesperaduros";
//            mensaje = entrada.nextLine();
//            entrada.close();
            System.out.println("Mensaje enviado -> " + mensaje);
            out.writeUTF(mensaje);
            cs.close();//Fin de la conexión
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}