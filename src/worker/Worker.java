package worker;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Libreries.Usuario;

/**
 *
 * @author Lorena Cáceres Manuel
 * @author Fredy Vargas Flores
 */
public class Worker extends Thread {

    private Socket socket=null;
 
    public Worker(Socket Sclient) {
            this.socket = Sclient;  
    }
    
    public void run(){
        try {
            //Declaramos el flujo de entrada de datos del Socket
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); 
            //Declaramos el flujo de salida de datos del Socket
            ObjectOutputStream oos = new ObjectOutputStream (socket.getOutputStream());
            //Instanciamos un Objeto de tipo Usuario y lo leemos
            Usuario usuario = (Usuario) ois.readObject();
            //Lo sacamos por pantalla 
            System.out.println("Me llega el servidor el nombre " + usuario.getUsuario());
            //Instanciamos otro Objeto de tipo Usuario y le pasamos el String Tartanga
            Usuario usuarioR = new Usuario("Tartanga");
            //Escribimo el objeto que hemos instanciado anteriormente -- Sacará por pantalla (Cliente): Tartanga
            oos.writeObject(usuarioR);
            //Cerramos el Socket
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}