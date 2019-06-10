package Calendar.database;
import java.sql.*;

/**
 * Klasa sluzaca do polaczenia z baza danych
 */
public class SQLData {
		private static final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=calendar;integratedSecurity=true;";

		protected Connection con = null;  
		protected Statement stmt = null; 
		protected ResultSet rs = null;  
		
		/**
		 * Metoda laczaca z baz¹ danych
		 * @throws Exception
		 */
		protected void connect() throws Exception
		{
			try
		    {  
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
				con = DriverManager.getConnection(connectionUrl);   
		    }
			catch (Exception e)
		    {
				throw new Exception("Wyst¹pi³ b³¹d po³¹czenia z baz¹ danych. SprawdŸ swoje po³¹czenie z internetem");
			}   	
		}

		/**
		 * Metoda konczaca polaczenie z baza danych
		 * @throws Exception
		 */
		protected void disconnect() throws Exception
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch(Exception e) {
					throw new Exception("Wyst¹pi³ b³¹d zamkniêcia z baz¹ danych");
				}  
			}
		}

}
