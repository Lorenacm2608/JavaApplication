package controlDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lorena Cáceres Manuel
 */
public class PoolConnection {

    private List<Connection> conexionesDisponibles = new ArrayList<>();
    private List<Connection> conexionesUsadas = new ArrayList<>();
    private final int conexionesMaximas = 5;
    //private final int conexionesMinimas= 1;

    public PoolConnection() throws SQLException {
        for (int i = 1 ; i < conexionesMaximas; i++) {
            conexionesDisponibles.add(this.crearConexion());
        }
    }

    /**
     * Con esta funcion, crearemos nuevas conexiones de manera interna *
     */
    private Connection crearConexion() {
        Configuration configuration = Configuration.getInstance(); //getinstance devuelve un tipo configuracion el cual guarda los valores para conectarnos a la BD
        try {
            Class.forName(configuration.driver);
            Connection connection = (Connection) DriverManager.getConnection(configuration.url, configuration.user, configuration.password);
            return connection;
        } catch (SQLException e) {
            Logger.getLogger(PoolConnection.class.getName()).log(Level.SEVERE, null, e); //CAMBIAR LOGGER
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PoolConnection.class.getName()).log(Level.SEVERE, null, ex); //CAMBIAR LOGGER
        }
        return null;
    }

    /**
     * Con esta funcion, usaremos las conexiones creadas y disponibles del poo
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
     * añadido a las conexiones usadas *
     */
    public boolean devolverConexion(Connection con) {
        if (con != null) {
            conexionesUsadas.remove(con);
            conexionesDisponibles.add(con);
            return true;
        }
        return false;
    }

    /**
     * Con esta funcion, sabremos cuantas conexiones tenemos disponible
     *
     * @return numero de conexiones disponibles que tenemos en nuestro pool*
     */
    public int numeroConexionesDisponibles() {
        return conexionesDisponibles.size();
    }

}
