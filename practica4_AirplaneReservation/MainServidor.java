package practica4_AirplaneReservation;

import java.io.IOException;

//Clase principal que hará uso del servidor
public class MainServidor {
//	protected static Servidor serv;
//	
//	protected Servidor getServ() {
//		return serv;
//	}
	public static void main(String[] args) throws IOException {
		Servidor serv = new Servidor(); //Se crea el servidor
		
		System.out.println("Iniciando servidor\n");
		serv.startServer(); //Se inicia el servidor
    }
	  
}