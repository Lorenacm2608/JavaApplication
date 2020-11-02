package controlDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lorena Cáceres Manuel
 * @author Fredy Vargas Flores
 * 
 */
public class PoolConnection {

    private ResourceBundle rb = ResourceBundle.getBundle("controlDB.Configuration");
    public Connection poolConnection;
    private int numConexiones = 0;      //Numero de conexiones activas
    public ArrayList<Connection> conexionesDisponibles = new ArrayList<>();
    public ArrayList<Connection> conexionesUsadas = new ArrayList<>();
 
    public PoolConnection() throws SQLException {
        
    }

    /**
     * Esta funcion, nos pregunta si la conexion existe, en el caso de
     * que sea null, crearemos una nueva conexion y la retornaremos. Sino le devolveremos el que hemos
     * estado usando *
     */
    public synchronized Connection getInstance() {
        Connection con = null;
        con = getConnection();
        if (con == null) {
            if (con == null) {
                con = conexionPool();
            }
            con = habilitarConexion(con);
        }
        return con;
    }

    /**
     * Con esta funcion, crearemos nuevas conexiones de manera interna *
     */
    private Connection crearConexion() {
        try {
            Class.forName(rb.getString("driver")); 
            Connection connection = (Connection) DriverManager.getConnection(rb.getString("url"), rb.getString("user"), rb.getString("password"));
            return connection;

        } catch (SQLException e) {
            Logger.getLogger(PoolConnection.class
                    .getName()).log(Level.SEVERE, null, e); //CAMBIAR LOGGER

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PoolConnection.class
                    .getName()).log(Level.SEVERE, null, ex); //CAMBIAR LOGGER
        }
        return null;
    }

    /**
     * Con esta funcion, usaremos las conexiones creadas y disponibles del pool
     *
     * @return si retorna nulo, nos saldrá el mensaje de "No hay conexiones
     * disponibles", en el caso contrario, nos retornaria una conexion la cual
     * podremos usar contra la base de datos*
     */
    public Connection getConnection() {
        if (conexionesDisponibles.isEmpty()) {
            Logger log = Logger.getLogger("No hay conexiones disponibles");
            return null;
        } else {
            Connection con = conexionesDisponibles.remove(conexionesDisponibles.size() - 1);
            conexionesUsadas.add(con);
            return con;
        }
    }

    /**
     * Con esta funcion, devolveremos una conexion al pool
     *
     * @param con es un objeto de tipo Connection, si es distinto a null
     * encargará de borrar una de las usadas y añadirse a las conexiones
     * disponibles
     * @return true: hemos borrado una de las conexiones usadas y la hemos
     * añadido a las conexiones disponibles *
     */
    public synchronized boolean devolverConexion(Connection con) {
        if (con != null) {
            borrarConexion(conexionesUsadas, con);
            conexionesDisponibles.add(con);
            return true;
        }
        return false;
    }
    public synchronized void closeConnection(Connection con) {
        if (con != null) {
            borrarConexion(conexionesUsadas, con);
            conexionesDisponibles.add(con);     
        }
       
    }

    /**
     * Con esta funcion, sabremos cuantas conexiones tenemos disponible
     *
     * @return numero de conexiones disponibles que tenemos en nuestro pool*
     */
    public int numeroConexionesDisponibles() {
        return conexionesDisponibles.size();
    }
    
    /**
     * Esta funcion se encarga de añadir conexiones dentro de nuestro pool 
     * @return una conexion nueva y la añade a nuestro ArrayList de conexiones usadas
     */

    private Connection conexionPool() {
        Connection con = null;
        con = crearConexion();
        numConexiones++;
        conexionesUsadas.add(con);

        return con;
    }

    /**
     * Con esta funcion comprobamos si la conexion esta activa para poder
     * reutilizarla contra la base de datos
     *
     * @param con es la conexión que le pasamos para su comprobar su actividad
     * @return si es true, devolverá la misma conexión pasada, en cambio si es
     * false, creará una nueva y la añadirá a conexiones usadas
     */

    private Connection habilitarConexion(Connection con) {
        if (conexionDisponible(con)) {

        } else {
            borrarConexion(conexionesUsadas, con);
            numConexiones--;
            con = crearConexion();
            conexionesUsadas.add(con);
            numConexiones++;   
        }
        return con;
    }
    /**
     * A esta funcion recibe como parametros un arraylist de conexiones y una conexion, recorremos dicho arraylist
     * en busca de la posicion de esa conexion, si la encontramos, la borraremos del arraylist
     * @param conexionesUsadas --> ArrayList de conexiones 
     * @param con --> Conexion que queremos borrar
     */

    public void borrarConexion(ArrayList<Connection> conexionesUsadas, Connection con) {
        for (int i = 0; i < conexionesUsadas.size(); i++) {
            if (conexionesUsadas.get(i) == con) {
                conexionesUsadas.remove(i);
                break;
            }
        }
    }
    /**
     * Esta funcion nos permite comprobar si la conexion esta activa y funcionable 
     * @param con --> Conexion que queremos comprobar
     * @return true, significa que la conexion esta disponible
     * @return false, significa que la conexion no está disponible
     */

    private boolean conexionDisponible(Connection con) {
        try (Statement st = con.createStatement()) {
            st.executeQuery("select 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}