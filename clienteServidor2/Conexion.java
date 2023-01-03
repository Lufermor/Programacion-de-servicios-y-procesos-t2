package clienteServidor2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {
    private final int PUERTO = 1234; //Puerto para la conexión
//    private final String HOST = "localhost"; //Host para la conexión
    private final String HOST = "192.168.14.75";
    protected ServerSocket ss; //Socket del servidor
    protected Socket cs; //Socket del cliente
    
    public Conexion(String tipo) throws IOException {//Constructor
        if(tipo.equalsIgnoreCase("servidor")) {
            ss = new ServerSocket(PUERTO);//Se crea el socket para el servidor en puerto 1234
            //cs = new Socket(); //Socket para el cliente
        } else {
            cs = new Socket(HOST, PUERTO); //Socket para el cliente en localhost en puerto 1234
        }
    }
}