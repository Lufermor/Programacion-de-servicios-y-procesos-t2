package distributedChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

/*
 * Pre: ---
 * Post: Clase que representa a cada cliente que se quiere conectar al servidor,
 * gestiona la comunicación con el servidor y las opciones que se muestran a 
 * cada cliente como interfaz de usuario.
 */
public class Cliente  extends Conexion{
    private String nombre;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PublicKey serverPublicKey;
    private String salaID;
    DataInputStream in;
    DataOutputStream out;
    
	public Cliente() throws IOException {
		super("cliente");
	}
	
	/*
	 * Pre: ---
	 * Post: método que se ejecuta al iniciar el cliente, crea las claves
	 * pública y privada, crea los canales de entrada y salida para comunicarse
	 * con el servidor.
	 * Recibe e imprime el mensaje de confirmaciónd de conexión del servidor
	 * Obliga al usuario a establecer un nickName con el método selectNickname()
	 * Comienza un bucle while que le muestra el menú al usuario y le pide 
	 * escoger una opción.
	 * Llama al método adecuado según la opción escogida por el usuario, hasta que 
	 * este escoge cerrar sesión.
	 */
	public void startClient() {//Método para iniciar el cliente
		try {
            // Enviamos la clave pública del cliente al servidor y recibimos la del servidor.
            establecerCanalesYClaves();
            
            Scanner scanner = new Scanner(System.in);
            selectNickname(in, out, scanner);
            boolean cerrarSesion = false;
            while (!cerrarSesion) {
                System.out.println("Welcome to Salesianos chat!\nSelect an option (insert only the number):"
                		+ "\n\t1. Join a room"
                		+ "\n\t2. Create new room"
                		+ "\n\t3. List rooms"
                		+ "\n\t4. Change nickname"
                		+ "\n\t5. Disconnect");
                String opcion = scanner.nextLine().strip().toLowerCase();
            	switch (opcion) {
					case "1":
						seleccionarSala(scanner, out, in);
						break;
					case "2":
						crearNuevaSala(scanner, out, in);
						break;
					case "3":
						listarSalas(in, out);
						break;
					case "4":
						selectNickname(in, out, scanner);
						break;
					case "5":
						cerrarSesion(cs, scanner, out);
						cerrarSesion = true;
						break;
					default:
						System.out.println("Invalid option");
						break;
				}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Pre: Se ha conectado un cliente al servidor
	 * Post: Establece los canales de entrada y salida, crea las 
	 * claves pública y privada, le envía la clave pública al servidor
	 * y recibe la clave pública del servidor.
	 */
	private void establecerCanalesYClaves() {
		try {
			// Generamos un par de claves RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            //System.out.println("Clave public cliente: " + publicKey);
			in = new DataInputStream(cs.getInputStream());
			out = new DataOutputStream(cs.getOutputStream());
	        System.out.println(in.readUTF() + "\n\tSession started");
	        
	        //Convertimos la clave pública a byte[]
	        byte[] bytePublicKey = publicKey.getEncoded();
	        //System.out.println("\nBYTE KEY: " + bytePublicKey);
	        
	        //Clave en byte[] a String 
	        String strKey = Base64.getEncoder().encodeToString(bytePublicKey);
	        //Enviamos clave pública en string al server.
	        out.writeUTF(strKey);
	        
	        // Ahora recibimos la clave pública del server:
	        String serverPublicKeyString = in.readUTF();
	        // Convertir la clave pública del cliente de String a PublicKey
	        byte[] serverPublicKeyBytes = Base64.getDecoder().decode(serverPublicKeyString);
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(serverPublicKeyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        this.serverPublicKey = keyFactory.generatePublic(publicKeySpec);
	        
	        System.out.println("Public keys shared\n");
	        
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Pre: ---
	 * Post: Se le pide al cliente que diga un nombre de sala existente, 
	 * se comprueba que no tiene espacios, se envía este nombre al metodo unirseASala.
	 */
	private void seleccionarSala(Scanner scanner, DataOutputStream out, DataInputStream in) {
		String nombreSala = "";
		while(true) {
			System.out.println("\nInsert room name or type \"CANCEL\" to cancel joining:");
			nombreSala = scanner.nextLine().strip();
			if(nombreSala.contains(" ")) {
				System.out.println("The room name can't contain white spaces");
				continue;
			}else if(nombreSala.length()<1) {
				System.out.println("Room name invalid");
				continue;
			}else if(nombreSala.equals("CANCEL")) return;
			break;
		}
		unirseASala(scanner, in, out, nombreSala);
	}

	/*
	 * Pre: El nombre de la sala ha sido introducido por el cliente
	 * Post: Se envía el nombre de la sala al servidor, junto con el comando JOIN_ROOM. 
	 * El servidor comprueba que la sala existe y que se tiene acceso 
	 * (a implementar comprobación de password). Si todo está bien
	 * El servidor envía un mensaje afirmativo y se actualiza la sala del cliente.
	 * A continuación se crean los hilos de escucua y escritura para el cliente, y se inician.
	 * Se espera hasta que los hilos finalicen, esto significa que el cliente se ha desconectado
	 * de la sala. Por último se elimina la sala anotada en el cliente.
	 *  
	 */
	public void unirseASala(Scanner scanner, DataInputStream in, DataOutputStream out, String nombreSala){
		try {
			out.writeUTF("JOIN_ROOM " + nombreSala);
			String respuestaServidor = in.readUTF();
			// Se realiza la verificación de si la sala es privada, y en caso afirmativo, pide la contraseña
			String password = "";
			// Variable que usamos para saber si imprimirle al usuario el mensaje de contraseña incorrecta
			boolean primeraRonda = true;
			while (respuestaServidor.equals("INSERT_PASSWORD")) {
				if (!primeraRonda) System.out.println("\nIncorrect password, try again");
				while (true) {
					System.out.println("\nType room password or type \"CANCEL\" to cancel joining:");
					password = scanner.nextLine().strip();
					if (password.contains(" ") || password.length() < 1) {
						System.out.println("Invalid password, try again");
						primeraRonda = false;
						continue;
					}
					if (password.equals("CANCEL")) {
						out.writeUTF("CANCEL");
						System.out.println("Aborting joining room\n");
						return;
					} else {
						// Enviamos la contraseña hasheada al server
						out.writeUTF("" + password.hashCode());
					}
					respuestaServidor = in.readUTF();
					break;
				}
				primeraRonda = false;
				
			}
			if(respuestaServidor.equals("JOINED_ROOM")) {
				this.salaID = nombreSala;
				HiloClienteEscucha hiloClienteEscucha = new HiloClienteEscucha(in, this, nombreSala);  
				HiloClienteEscribe hiloClienteEscribe = new HiloClienteEscribe(scanner, out,this, nombreSala);
				hiloClienteEscucha.start();
				hiloClienteEscribe.start();
				hiloClienteEscribe.join();
				hiloClienteEscucha.join();
			}else {
				System.out.println("\nRoom not found\n");
			}
		} catch (IOException e1) {
			System.out.println("\nError comunicating with server at trying to join room");
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.salaID = null;
	}

	/*
	 * Pre: ---
	 * Post: Solicita al usuario un nombre de sala, 
	 * Pregunta al usuario si la sala es privada, si lo es, le solicita también 
	 * el password. Realiza una validación básica para el password.
	 * Una vez establecidos los parámetros, se le envían todos en un mensaje al servidor,
	 * junto con el comando NUEVA_SALA
	 *  
	 */
	public void crearNuevaSala(Scanner scanner, DataOutputStream out, DataInputStream in){
		System.out.println("\nType new room name:");
		String nombreSala = "";
		//Se pide el nombre de la sala al cliente
		while(true) {
			nombreSala = scanner.nextLine().strip();
			if(nombreSala.contains(" ")) {
				System.out.println("The password can't contain white spaces, "
						+ "choose another password");
				continue;
			}else break;
		}
		//Se pregunta al cliente si la sala es privada, y si lo es, se le pide una contraseña
		String password = "";
		System.out.println("Make room private? (Y/N)");
		String privada = scanner.nextLine().strip();
		boolean esPrivada = privada.equalsIgnoreCase("Y") || privada.equalsIgnoreCase("Yes");
		if(!esPrivada) System.out.println("Setting room public");
		else {
			System.out.println("Setting room private \nType room password");
			while(true) {
				password = scanner.nextLine().strip();
				if(password.contains(" ")) {
					System.out.println("The password can't contain white spaces, "
							+ "choose another password");
					continue;
				}else if(password.length() < 1) {
					System.out.println("Password is too short, choose another password");
					continue;
				}else break;
			}
		}
		try {
			out.writeUTF("NUEVA_SALA " + nombreSala + " " + esPrivada + " " + password.hashCode());
			String respuestaServidor = in.readUTF();
			if(respuestaServidor.equals("ERROR_CREATING_SALA")) 
				System.out.println("Error creating sala");
			else if(respuestaServidor.equals("SALA_NAME_OCCUPIED")){
				System.out.println("Sala name already in use.");
			}else {
				System.out.println("Sala created: " + nombreSala);
			}
		} catch (IOException e) {
			System.out.println("Error creating sala");
			e.printStackTrace();
		}
		listarSalas(in, out);
	}

	/*
	 * Pre: ---
	 * Envía al servidor el comando para listar las salas disponibles
	 * Recibe del servidor e imprime por pantalla, las id de las salas
	 * disponibles combinadas en un solo String.
	 */
	public void listarSalas(DataInputStream in, DataOutputStream out) {
		try {
			out.writeUTF("LISTAR_SALAS");
			System.out.println("\nRooms availables: \n--");
			System.out.println(in.readUTF());
		} catch (IOException e) {
			System.out.println("Error listing rooms");
			e.printStackTrace();
		}
		
	}

	/*
	 * Pre: El cliente quiere enviar un mensaje al servidor
	 * Post: Se encripta el mensaje y se devuelve para enviarlo al server. 
	 */
	public String encriptar(String mensaje) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
			byte[] bytesEncriptados = cipher.doFinal(mensaje.getBytes());
			String mensajeEncriptado = Base64.getEncoder().encodeToString(bytesEncriptados);
			return mensajeEncriptado;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Pre: El servidor ha enviado un mensaje encriptado
	 * Post: Se desencripta el mensaje y se devuelve desencriptado
	 */
	public String desencriptar(String mensajeEncriptado) {
	    try {
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        byte[] mensajeBytes = Base64.getDecoder().decode(mensajeEncriptado);
	        byte[] bytesDesencriptados = cipher.doFinal(mensajeBytes);
	        return new String(bytesDesencriptados);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/*
	 * Pre: Los canales de entrada y salida de mensajes con el servidor han sido creados  
	 * Post: Este método pregunta al usuario por un nuevo nickname, se lo envía al servidor, 
	 * el servidor verifica en su lista de nombres activos, si el nombre está disponible,
	 * devuelve un mensaje afirmativo, sino, devuelve un mensaje negativo.
	 * El usuario deberá introducir nicknames de nuevo hasta que seleccione uno disponible.
	 */
	private void selectNickname(DataInputStream in, DataOutputStream out, Scanner scanner) {
		while(true) {
			System.out.println("\nType your new nickname: ");
			String nickname = scanner.nextLine().strip();
			if(nickname.contains(" ")) {
				System.out.println("The nickname can't contain white spaces");
				continue;
			}
			String mensaje = "";
			//Comprobamos si el cliente ya tiene nombre para que pueda escoger el mismo si lo desea
			if(this.nombre == null) mensaje = "SELECT_NICKNAME " + nickname;
			else mensaje = "SELECT_NICKNAME " + nickname + " " + this.nombre;
			try {
				out.writeUTF(mensaje);
				String respuesta = in.readUTF();
				if(respuesta.equals("NICKNAME_SET")) {
					this.nombre = nickname;
					System.out.println("Nickname set: " + nickname + "\n");
					break;
				} else if(respuesta.equals("ERROR")){
					System.out.println("Nickname invalid, pick another one\n");
				}else System.out.println("Nickname already in use, pick another one\n");
			} catch (IOException e) {
				System.out.println("Nickname invalid, try again\n");
				e.printStackTrace();
			}
		}
	}

	/*
	 * Pre: El usuario ha seleccionado la opción de finalizar sesion
	 * Post: Se envía un mensaje al servidor que le indica que cierre la conexión
	 * con el cliente, se cierra la conexión con el servidor y el escáner.
	 */
	private void cerrarSesion(Socket cs, Scanner scanner, DataOutputStream out) {
		System.out.println("Finishing session");
		
		try {
			out.writeUTF("FINISH_SESION" );
			cs.close();
			scanner.close();
			System.out.println("Disconnected");
		} catch (IOException e) {
			System.out.println("Ending session failed, try again");
			e.printStackTrace();
		}
		
	}
	
	//Getters, Setters

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getSalaID() {
		return salaID;
	}

	public void setSalaID(String salaID) {
		this.salaID = salaID;
	}
	
}
