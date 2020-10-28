
package mainServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import worker.Worker;

/**
 *
 * @author Fredy Vargas Flores
 * @author Lorena Cáceres Manuel
 */
public class ApplicationServer {

    private static final int port = 5555;
    //!--AÑADIR LIMITE DE HILOS
    public static void main(String[] args) throws IOException {
       new ApplicationServer().iniciarServer();
       
}
    public void iniciarServer() throws IOException{
        ServerSocket ss = new ServerSocket(port);
        System.out.println("[SERVER] Iniciando Servidor...");
        while(true){
            Socket socket = ss.accept(); //Aceptaremos al cliente
            System.out.println("[SERVER] Cliente aceptado!");
            new Worker (socket).start(); //Llamamos al worker y le pasamos el socketS
        }
    }
}