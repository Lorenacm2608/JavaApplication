package mainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Worker;

/**
 *
 * @author Fredy Vargas Flores
 * @author Lorena Cáceres Manuel
 */
public class ApplicationServer {

    private int limiteHilo = 2;
    private int actualHilo = 0;
    private static final int port = 3338;

    //!--AÑADIR LIMITE DE HILOS
    public static void main(String[] args) throws IOException {
        new ApplicationServer().iniciarServer();

    }

    public void iniciarServer() throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("[SERVER] Iniciando Servidor...");
        int hActuales = 0;
        while (true) {
            Socket socket = ss.accept(); //Aceptaremos al cliente

            if (actualHilo < limiteHilo) {
                System.out.println("[SERVER] Cliente aceptado!");
                Worker worker = new Worker(socket); //Llamamos al worker y le pasamos el socket
                actualHilo++;
                try {
                    worker.join();
                    actualHilo--;
                } catch (InterruptedException ex) {
                    Logger.getLogger(ApplicationServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Siento decirte que no hay hilos disponibles ");
            }
        }
    }
}