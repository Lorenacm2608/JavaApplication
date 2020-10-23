package controlDB;
/**
 * @author Lorena Cáceres Manuel
 */
public class Configuration {

 public final String user;
 public final String password ;
 public final String url;
 public final String driver;
 public final Integer numConexionesMaximas=5;
  
 /**Inicializamos los valores de la base de datos para poder conectarnos a ella**/
 public Configuration(){
     user="root";
     password="abcd*1234";
     url="jdbc:mysql://localhost:3306/reto1";
     driver="com.mysql.jdbc.Driver";
 }
  
 private static final Configuration configuration = new Configuration();
  
 public static Configuration getInstance(){ 
  return configuration;
 }
}