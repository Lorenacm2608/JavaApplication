package mainServer;

import controlDB.PoolConnection;
import controlDB.PoolPila;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author 2dam
 */
public class ApplicationServer {

    public static void main(String[] args) throws SQLException {
        PoolPila pool = new PoolPila();
        Connection con = null;
        // for (int i = 1; i < 7; i++) {
        //  con1 = pool.getConnection();
        //   pool.devolverConexion(con1);
        // System.out.println(pool.conexionesDisponibles.size()); 
        String select="select * from tartanga";
        try {
            con = pool.getConnection();
            try (PreparedStatement ps = con.prepareStatement(select)) {
                ResultSet rs = ps.executeQuery();
                //System.out.println("There are below tables:");
                while (rs.next()) {
                    String nombre = rs.getString(1);
                    System.out.println(nombre);
                }
            }
        } finally {
            if (con != null) {
                pool.devolverConexion(con);
            }
        }

        //  }
    }

    /*   
        if(pool!=null){
            
            System.out.println("Conectado!");
            
        }else{
            System.out.println("No conectado");
        }*/
}
