package practica4_AirplaneReservation;

import java.io.IOException;
import java.util.ArrayList;

public class Servidor extends Conexion { //Se hereda de conexión para hacer uso de los sockets y demás
	protected ArrayList<Thread1> clientes = new ArrayList<Thread1>();
	protected AirplaneSeats vuelo;
	protected boolean finalizar;
	
	protected ArrayList<Thread1> getClientes(){
		return clientes;
	}
	
    public Servidor() throws IOException {
    	super("servidor");
    	vuelo = new AirplaneSeats();
    	finalizar = false;
    }

    public boolean isFinalizar() {
		return finalizar;
	}

	public void setFinalizar(boolean finalizar) {
		this.finalizar = finalizar;
	}

	public void startServer() {//Método para iniciar el servidor
        try {
        	while(true) {
        		if(finalizar) break;
        		System.out.println("Server waiting..."); //Esperando conexión
//                cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
        		Thread1 hilo = new Thread1(ss.accept(), clientes, vuelo, this);
        		hilo.start();
                System.out.println("Client online");
                // se envía el arrayList para que el hilo pueda borrarse de este al finalizar:
                clientes.add(hilo); 
                System.out.println("Clients online: " + clientes.size());
                if(clientes.size()>=10) {
                	System.out.println("Server bussy, waiting for a client to log off.");
                	while(clientes.size()>=10);
                }
        	}
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}
