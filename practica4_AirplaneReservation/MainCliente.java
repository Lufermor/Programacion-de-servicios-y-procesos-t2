package practica4_AirplaneReservation;

import java.io.IOException;
import java.util.ArrayList;

//Clase principal que hará uso del cliente
public class MainCliente {
  public static void main(String[] args) throws IOException {
	  ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	  for(int i = 0; i < 4; i++) {
		  Cliente cli = new Cliente("Cliente" + i); //Se crea el cliente

	      System.out.println("Starting client n \n" + i);
	      cli.startClient(); //Se inicia el cliente

		  clientes.add(cli);
	  }
  }
}