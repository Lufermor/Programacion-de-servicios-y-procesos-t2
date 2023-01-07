package practica4_AirplaneReservation;

import java.io.IOException;
import java.util.ArrayList;

public class Servidor extends Conexion { //Se hereda de conexión para hacer uso de los sockets y demás
	protected ArrayList<Thread1> clientes = new ArrayList<Thread1>();
	
	protected ArrayList<Thread1> getClientes(){
		return clientes;
	}
	
    public Servidor() throws IOException {
    	super("servidor");
    }

    public void startServer() {//Método para iniciar el servidor
        try {
        	while(true) {
        		System.out.println("Esperando..."); //Esperando conexión
//                cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
        		Thread1 hilo = new Thread1(ss.accept(), clientes);
        		hilo.start();
                System.out.println("Cliente en línea");
                // se envía el arrayList para que el hilo pueda borrarse de este al finalizar:
                clientes.add(hilo); 
                System.out.println("Clientes en línea: " + clientes.size());
                if(clientes.size()>=10) {
                	System.out.println("Servidor ocupado, esperando que se libere un hilo.");
                	while(clientes.size()>=10);
                }
        	}
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}
