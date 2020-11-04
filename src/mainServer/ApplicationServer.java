package mainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Worker;

/**
 *
 * @author Fredy Vargas Flores
 * @author Lorena Cáceres Manuel
 */
public class ApplicationServer {

  
    private static int actualHilo = 0;
    private static final int port = 5555;
     static int MAX_HILO = 1;
   

    //!--AÑADIR LIMITE DE HILOS
    public static void main(String[] args) throws IOException {
        new ApplicationServer().iniciarServer();

    }

    public void iniciarServer() throws IOException {
       
        ServerSocket ss = new ServerSocket(port);
        System.out.println("[SERVER] Iniciando Servidor...");
           
        while (true) {
            Socket socket = ss.accept(); //Aceptaremos al cliente
            System.out.println("[SERVER] Cliente aceptado!");
            incrementarHilo();
                    Worker worker1= new Worker(socket);
            if(control()){
                
                    incrementarHilo();
                    Worker worker= new Worker(socket);
                     
                
            }else{
                System.out.println("No hay hilos disponibles");
            }
            
        }      
    }
    public synchronized boolean control(){
        boolean resu=false;
        if(actualHilo<MAX_HILO){
            resu=true;
        }
        return resu;
    }
    public synchronized  void incrementarHilo(){
        actualHilo++;
        System.out.println("total "+actualHilo);
    }
    synchronized static public void liberarHilo(){
        actualHilo--;
        System.out.println("total "+actualHilo);
    }

}
