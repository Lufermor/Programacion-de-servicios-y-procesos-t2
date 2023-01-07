package practica4_AirplaneReservation;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Clase principal que hará uso del cliente, lanza hilos para conseguir que 
 * varios clientes trabajen con el servidor simultaneamente.
 */
public class MainCliente {
  public static void main(String[] args) throws IOException {
	  ArrayList<Thread2_Clientes> clientes = new ArrayList<Thread2_Clientes>();
	  for(int i = 0; i < 4; i++) {
		  clientes.add(new Thread2_Clientes(i));
		  clientes.get(i).start();
	  }
	  for(int i = 0; i < 4; i++) {
		  try {
			clientes.get(i).join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	  }
  }
}