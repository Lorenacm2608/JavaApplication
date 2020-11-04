package worker;

import controlDB.DaoImplementation;
import exceptions.AutenticacionFallidaException;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.UsuarioExistenteException;
import exceptions.UsuarioNoEncontradoException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import libreries.Message;
import libreries.Signable;
import libreries.Tipo;
import libreries.Usuario;
import libreries.SignableFactory;
import mainServer.ApplicationServer;

/**
 *
 * @author Lorena Cáceres Manuel
 * @author Fredy Vargas Flores
 */
public class Worker extends Thread {

    private Socket socket = null;
    private Message msg;

    public Worker() {

    }

    public Worker(Socket sClient) {
        this.socket = sClient;
        this.start();
    }

    @Override
    public void run() {
       
            try {
                Message msg = null;
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;

                try {
                    DaoImplementation dao = new DaoImplementation();
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    msg = new Message();
                    msg = (Message) ois.readObject();

                    System.out.println(msg.getTipoMensaje() + " + " + msg.getUsuario().getUsuario());
                    switch (msg.getTipoMensaje()) {
                        case LOGIN:
                            dao.logIn(msg.getUsuario());
                            System.out.println("dao hecho");
                            msg.setTipoMensaje(Tipo.OK);
                            System.out.println("flujo escrito");
                            break;
                        case SIGNUP:
                            dao.signUp(msg.getUsuario());
                            System.out.println("dao hecho");
                            msg.setTipoMensaje(Tipo.OK);
                            System.out.println("flujo escrito");
                            break;
                    }
                } catch (ClassNotFoundException ex) {
                    msg.setTipoMensaje(Tipo.ERROR_BASE_DE_DATOS);
                } catch (AutenticacionFallidaException ex) {
                    msg.setTipoMensaje(Tipo.CONTRASENA_ERRONEA);
                } catch (ErrorBDException ex) {
                    msg.setTipoMensaje(Tipo.ERROR_BASE_DE_DATOS);
                } catch (ErrorServerException ex) {
                    msg.setTipoMensaje(Tipo.ERROR_SERVIDOR);
                } catch (UsuarioNoEncontradoException ex) {
                    msg.setTipoMensaje(Tipo.USUARIO_NO_EXISTE);
                } catch (UsuarioExistenteException ex) {
                    msg.setTipoMensaje(Tipo.USUARIO_EXISTENTE);
                } finally {
                    oos.writeObject(msg);
                    System.out.println(msg.getTipoMensaje());
                    oos.close();
                    ois.close();
                    socket.close();

                }
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                    ApplicationServer.liberarHilo();                   
            }
        
        

    }

}
