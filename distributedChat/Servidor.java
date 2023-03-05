package distributedChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * Pre: ---
 * Post: Clase que funciona como servidor, establece el socket y 
 * espera por conexiones de clientes, lanza un hilo por cada cliente 
 * conectado.
 */
public class Servidor extends Conexion {
    private Map<String, Sala> salas;
    private List<String> nombresClientes;
    private ArrayList<ThreadServidor> hilosServidor;

    public Servidor() throws IOException {
    	super("servidor");
        salas = new HashMap<>();
        nombresClientes = new ArrayList<>();
        hilosServidor = new ArrayList<ThreadServidor>();
    }

    /*
     * Pre: ---
     * Post: Espera por conexiones de clientes, lanza un hilo por cada cliente conectado
     * Notifica a la consola del servidor cada vez que se conecta un cliente
     */
    public void iniciar() {
        try {
        	while(true) {
        		System.out.println("Salesianos chat Server waiting..."); //Esperando conexión
        		ThreadServidor hilo = new ThreadServidor(ss.accept(), hilosServidor, salas, nombresClientes);
        		hilo.start();
                System.out.println("Client connected");
                // se envía el arrayList para que el hilo pueda borrarse de este al finalizar:  
                hilosServidor.add(hilo); 
                System.out.println("Clients online: " + hilosServidor.size());
        	}
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor");
            e.printStackTrace();
        }
    }

}
