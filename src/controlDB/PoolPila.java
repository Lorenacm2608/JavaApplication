package controlDB;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lorena Cáceres Manuel
 *
 * devolver/get --> sincronizado conexonesMaximas marcadas por hilos
 */
public class PoolPila {
    private ResourceBundle rb = ResourceBundle.getBundle("controlDB.Configuration");
    public static PoolPila pool;
    public Stack<Connection> conexionesDisponibles = new Stack<>();
    private Set<Connection> conexionesOcupadas = new HashSet<>();
    private int conexionesMaximas = 7;
    private int numeroConexion = 0;
    private Connection con;
    

    /**
     *
     * @throws SQLException
     */
    
    public PoolPila()  {
    }

    /**
     *
     * @return @throws SQLException
     */
    public synchronized Connection getConnection() throws SQLException {
        Connection con = null;
        if (estadoPool()) {
            throw new SQLException("Ninguna conexion disponible");
        }
        con = getConnectionPool();
        if (con == null) {
            con = crearConexionPool();
        }
        
        con = habilitado(con);
        
        return con;
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public synchronized void devolverConexion(Connection con) throws SQLException {
        
        if (con == null) {
            throw new NullPointerException("Null");
        }
        if (!conexionesOcupadas.remove(con)) {
            throw new SQLException("Conexion retornada ");
        }
        conexionesDisponibles.push(con);

    }

    /**
     *
     * @return
     */
    private synchronized boolean estadoPool() {
        boolean estado = false;
        if (conexionesDisponibles.size() == 0 && (numeroConexion >= conexionesMaximas)) {
            estado = true;
        }
        return estado;
    }

    /**
     *
     * @param con
     * @return
     */
    private Connection habilitado(Connection con) throws SQLException {
           System.out.println("1");
        if(estaDisponible(con)){
            return con;
        } 
            conexionesOcupadas.remove(con);
            numeroConexion--;
            con.close();
            con = crearConexion();
            conexionesOcupadas.add(con);
            numeroConexion++;
        
            

        return con;
    }
    private boolean estaDisponible(Connection con) {
		try (Statement  st = con.createStatement()) {
			st.executeQuery("select 1");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

    /**
     *
     * @return
     */
    private Connection crearConexionPool() {      
            Connection con = crearConexion();
            numeroConexion++;
            conexionesOcupadas.add(con);
                 
        return con;
    }

    /**
     *
     * @return
     */
    private Connection getConnectionPool() {
        Connection con = null;
        if (conexionesDisponibles.size() > 0) {
            con = conexionesDisponibles.pop();
            conexionesOcupadas.add(con);
        }
        return con;
    }

    /**
     * Con esta funcion, crearemos nuevas conexiones de manera interna *
     */
    
    
    private Connection crearConexion() {
       
        try {
            Class.forName(rb.getString("driver"));
            Connection con = (Connection) DriverManager.getConnection(rb.getString("url"), rb.getString("user"), rb.getString("password"));
            return con;

        } catch (SQLException e) {
            Logger.getLogger(PoolConnection.class
                    .getName()).log(Level.SEVERE, null, e); //CAMBIAR LOGGER

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PoolConnection.class
                    .getName()).log(Level.SEVERE, null, ex); //CAMBIAR LOGGER
        }
        return null;
    }
    public int numeroConexionesDisponibles() {
        return conexionesDisponibles.size();
    }


}
