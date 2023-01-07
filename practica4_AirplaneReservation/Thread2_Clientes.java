package practica4_AirplaneReservation;

import java.io.IOException;

public class Thread2_Clientes extends Thread {
	protected int i;

	public Thread2_Clientes(int i) {
		super();
		this.i = i;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
	
	public void run() {
		Cliente cli;
		try {
			cli = new Cliente("Cliente" + i);
			System.out.println("Starting client n " + i + "\n");
		    cli.startClient(); //Se inicia el cliente
		} catch (IOException e) {
			e.printStackTrace();
		} //Se crea el cliente
	}

	@Override
	public String toString() {
		return "Thread2_Clientes [clientNumber=" + i + "]";
	}
	
}
