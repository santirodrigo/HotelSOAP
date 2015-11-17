/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author sergi.soriano.bial
 */
@WebService(serviceName = "hotelWS")
public class hotelWS {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "consulta_libres")
    public int consulta_libres(@WebParam(name = "id_hotel") int id_hotel, @WebParam(name = "fecha") int fecha){
        //TODO write your implementation code here:
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hotelWS.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\UNI\\AD\\practica3.db");
            String selectStatement = "SELECT * " 
                                    + "FROM hotel_fecha "
                                    + "WHERE id_hotel=? AND fecha = ?";
            PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
            
            prepStmt.setString(1,Integer.toString(id_hotel));
            prepStmt.setString(2,Integer.toString(fecha));
            
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt("num_hab_libres");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(hotelWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
              if(connection != null)
                connection.close();
            }
            catch(SQLException e)
            {
              // connection close failed.
              System.err.println(e.getMessage());
            }
        }
        return -1;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "reserva_habitacion")
    public int reserva_habitacion(@WebParam(name = "id_hotel") int id_hotel, @WebParam(name = "fecha") int fecha) {
        //TODO write your implementation code here:
        //TODO write your implementation code here:
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\UNI\\AD\\practica3.db");
            String selectStatement = "SELECT * " 
                                    + "FROM hotel_fecha "
                                    + "WHERE id_hotel=? AND fecha = ?";
            PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
            
            prepStmt.setString(1,Integer.toString(id_hotel));
            prepStmt.setString(2,Integer.toString(fecha));
            
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next())
            {
                if (rs.getInt("num_hab_libres") > 0 )
                {
                    int habOcupadas = rs.getInt("num_hab_ocupadas")+1;
                    selectStatement = "UPDATE hotel_fecha " 
                                    + "SET num_hab_libres=?, num_hab_ocupadas=? "
                                    + "WHERE id_hotel=? AND fecha = ?";
                    prepStmt = connection.prepareStatement(selectStatement);
                    prepStmt.setString(1,Integer.toString(rs.getInt("num_hab_libres")-1));
                    prepStmt.setString(2,Integer.toString(habOcupadas));
                    prepStmt.setString(3,Integer.toString(id_hotel));
                    prepStmt.setString(4,Integer.toString(fecha));
                    prepStmt.executeUpdate();
                    return habOcupadas;
                }
                else
                {
                    return -1;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(hotelWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
              if(connection != null)
                connection.close();
            }
            catch(SQLException e)
            {
              // connection close failed.
              System.err.println(e.getMessage());
            }
        }
        return 0;
    }
}
