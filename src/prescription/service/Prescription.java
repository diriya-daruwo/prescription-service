package prescription.service;

import prescription.service.ResultSetConverter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.JSONArray;



@Path("/prescription")
public class Prescription {
/*	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/hello_new")
	public String sayHelloPlain()
	{
		try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prescription","root","abc123");	 
		Statement st=con.createStatement();
		 
		String query="select * from Medicine";
		 
		ResultSet rs=st.executeQuery(query);
		rs.next();
		return rs.getString(2);
		}
		catch(Exception ex)
		{
			return ex.toString();
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHelloHTML()
	{
		return "Hello World 2";
	} */
	
	@GET
    @Path("/getPrescription/{id}")
    public Response getPrescription(@PathParam("id") long id) throws Exception{
		ResultSet result = null; 
		Statement stm = null;
		Connection con = null;
		JSONArray send = null;
		try {
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prescription","root","abc123");	 
		stm=con.createStatement();
		
        
            stm = con.createStatement();
            String sqlQuery = "select a.AppointmentNumber, a.IssueDate, b.MedicineName, b.Quantity from Prescription_Info a, Medicine b where a.AppointmentNumber=? and a.PrescriptionID=b.PrescriptionID";
            PreparedStatement preparedStmt = con.prepareStatement(sqlQuery);
            preparedStmt.setLong(1, id);            
            
            result = preparedStmt.executeQuery();
            send = ResultSetConverter.convertResultSetIntoJSON(result);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return Response.status(200).entity(send.toString()).build();
      //  return Response.ok(send.toString(), MediaType.APPLICATION_JSON).build();
    }
	
	@GET
    @Path("/deletePrescription/{id}")
    public Response deletePrescription(@PathParam("id") long id) throws Exception{ 
		Statement stm = null;
		Connection con = null;
		try {
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prescription","root","abc123");	 
		stm=con.createStatement();
		
        
            stm = con.createStatement();
            String sqlQuery = "Delete from Prescription_Info where PrescriptionID=?";
            PreparedStatement preparedStmt = con.prepareStatement(sqlQuery);
            preparedStmt.setLong(1, id);            
            preparedStmt.execute();
            System.out.println(sqlQuery);
            
        } catch (Exception ex) {
            ex.toString();
        }finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
		return Response.status(200).entity("Successfully deleted the prescription with id "+id).build();
    }
	
	@POST
    @Path("/addPrescriptionInfo")
    public Response createChanneling(
            @FormParam("appointmentId") long appointmentId,
            @FormParam("appointmentDate") String appointmentDate) throws SQLException{
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");        
        java.sql.Timestamp sqlDate = null;
        try {
            Date formattedDate = sdf.parse(appointmentDate);
            sqlDate = new java.sql.Timestamp(formattedDate.getTime());
        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }
        
       
        Connection con = null;
        Statement stm = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
    		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prescription","root","abc123");	 
    		stm=con.createStatement();
            
    		String sqlQuery = "Insert into Prescription_Info(AppointmentNumber,IssueDate) values(?,?);";
            PreparedStatement preparedStmt = con.prepareStatement(sqlQuery);
            preparedStmt.setLong(1, appointmentId);
            preparedStmt.setTimestamp(2, sqlDate);
            preparedStmt.execute();
            
            
            
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }       
        
        return Response.status(Response.Status.CREATED).entity("PrescriptionInfo "
                + "created on :"+appointmentDate).build();
    }
	@POST
    @Path("/addPrescriptionData")
    public Response createChanneling(
            @FormParam("prescriptionId") long prescriptionId,
            @FormParam("name") String name,
            @FormParam("Quantity") int Quantity) throws SQLException{
        
        Connection con = null;
        Statement stm = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
    		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prescription","root","abc123");	 
    		stm=con.createStatement();
            
    		String sqlQuery = "Insert into Medicine values(?,?,?);";
            PreparedStatement preparedStmt = con.prepareStatement(sqlQuery);
            preparedStmt.setLong(1, prescriptionId);
            preparedStmt.setString(2, name);
            preparedStmt.setInt(3, Quantity);
            preparedStmt.execute();
                       
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }       
        
        return Response.status(Response.Status.CREATED).entity("PrescriptionData "
                + "created for "+name).build();
    }
}
