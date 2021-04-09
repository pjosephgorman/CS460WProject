package JavaSrc.Data;

import JavaSrc.Exceptions.DuplicateUsernameException;
import JavaSrc.Util;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

@SuppressWarnings("SqlResolve")
public class SQLHandler
{
	private static final String hostname = "localhost";
	private static final String hostport = "1433";
	private static final String adminname = "admin";
	private static final String adminpwd = "admin";
	
	public static void init()
	{
		try
		{
			Connection c = connect();
			try
			{
				//Create table
				String initStr = //TODO Fill out tables, fill out final init vals -V
						"""
						IF(NOT EXISTS(SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users'))
						BEGIN
							CREATE TABLE users (
								user_id    INT                     IDENTITY(0,1)   PRIMARY KEY,
								uname      VARCHAR(32)  NOT NULL,
								pwd        CHAR(128)    NOT NULL
							);
							INSERT INTO users (uname, pwd) VALUES
								('foo','%s'),
								('bar','%s')
						END""".formatted(Util.hash("pineapple"), Util.hash("banana"));
				
				update(c, initStr);
				
				System.out.printf("""
				                  %b,%b,%b,%b%n""", loginUnhashed(c, "foo", "pineapple"), loginUnhashed(c, "bar", "pineapple"), loginUnhashed(c, "foo",
						"banana"), loginUnhashed(c, "bar", "banana"));
				
				new UserInfo(0);
				new UserInfo(1);
			}
			catch(Exception e)
			{
				Util.trace(e);
			}
		}
		catch(SQLException e)
		{
			Util.error(e.getMessage());
			System.exit(1);
		}
	}
	
	private static void clear()
	{
		try
		{
			Connection c = connect();
			update(c, "DROP TABLE IF EXISTS users");
		}
		catch(SQLException e)
		{
			Util.error(e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) //Test function, wipes database and re-inits
	{
		clear();
		init();
	}
	
	public static Connection connect() throws SQLException
	{
		return connect(adminname, adminpwd); //TODO Something about this -V
	}
	
	public static Connection connect(String uname, String pwd) throws SQLException
	{
		SQLServerDriver.register(); //Ensure the driver is registered
		return DriverManager.getConnection("jdbc:sqlserver://" + hostname + ":" + hostport + ";" + "databaseName=rpm_system;", uname, pwd);
	}
	
	protected static int update(Connection c, String sql) throws SQLException
	{
		Statement s = c.createStatement();
		return s.executeUpdate(sql);
	}
	
	protected static ResultSet query(Connection c, String sql) throws SQLException
	{
		Statement s = c.createStatement();
		return s.executeQuery(sql);
	}
	
	public static boolean loginUnhashed(Connection c, String uname, String rawPwd)
	{
		return login(c, uname, Util.hash(rawPwd));
	}
	
	public static boolean login(Connection c, String uname, String hashedPwd) throws DuplicateUsernameException
	{
		try
		{
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery("SELECT pwd FROM users WHERE uname = '" + uname + "'");
			if(!r.next()) return false;
			if(!hashedPwd.equals(r.getString(1))) return false;
			if(r.next()) throw new DuplicateUsernameException();
		}
		catch(SQLException ignored)
		{
			return false;
		}
		return true;
	}

	static ResultSet fetchUserRow(int UserID)
	{
		try
		{
			Connection c = connect();
			return query(c, """
			                SELECT * FROM users WHERE user_id = %d""".formatted(UserID));
		}
		catch(SQLException ignored)
		{
			return null;
		}
	}
}
