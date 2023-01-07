package practica4_AirplaneReservation;

import java.util.concurrent.Semaphore;

/*
 * Pre: ---
 * Post: This class acts as board in which the server checks
 * whether a seat is aviable or not
 */
public class AirplaneSeats {
	protected String[] seats;
	protected Semaphore semaphore;
	
	public AirplaneSeats() {
		seats = "LLLL-LLLL-LLLL-LLLL".split("-");
		semaphore = new Semaphore(1);
	}

	public String[] getSeats() {
		return seats;
	}

	public void setSeats(String[] seats) {
		this.seats = seats;
	}
	
	/*
	 * Pre: ---
	 * Post: Gets a semaphore ticket, checks if there are available seats, 
	 * if there are, checks if the booked seat is available, if it is, reserves it
	 * else, notifies the user indicating the seats status.
	 */
	protected String reserve(String silla) {
		if(String.join("-", seats).equalsIgnoreCase("XXXX-XXXX-XXXX-XXXX")) {
			return "FLIGHT FULL";
		}
		
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int row = Integer.parseInt(silla.substring(0, 1))-1;
		int seatIndex = "ABCD".indexOf(silla.substring(1,2));
		char[] rowArray = seats[row].toCharArray();
		String seatStatus = "" + rowArray[seatIndex];
		if(seatStatus.equalsIgnoreCase("L")) {
			rowArray[seatIndex] = 'X';
			seats[row] = String.valueOf(rowArray);
			semaphore.release();
			return "RESERVED:" + silla;
		}
		semaphore.release();
		return "SEAT BUSY:" + String.join("-", seats);
		
	}
}
