package JavaSrc.Data;

import JavaSrc.Exceptions.DuplicateUsernameException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Exceptions.RPMException;
import JavaSrc.Util;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

import static JavaSrc.Data.Roles.*;

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
				if(query(c, "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users'").next()) return; //Already exists
				
				//Create table
				StringBuilder rolelist = new StringBuilder();
				for(Roles r : Roles.values())
				{ rolelist.append(",'").append(r.name()).append("'"); }
				rolelist.deleteCharAt(0); //Extra comma!
				update(c, """
				          CREATE TABLE users (
				          	user_id    INT                     IDENTITY(0,1)   PRIMARY KEY,
				          	uname      VARCHAR(32)  NOT NULL                   UNIQUE,
				          	pwd        CHAR(128)    NOT NULL,
				          	fname      VARCHAR(64)  NOT NULL,
				          	lname      VARCHAR(64)  NOT NULL,
				          	role       VARCHAR(20)  NOT NULL CHECK(role IN (%s))
				          );""".formatted(rolelist));
				
				//Test values
				createUser(c, "foo", "pineapple", true, "a", "b", Admin);
				createUser(c, "bar", "banana", true, "a", "b", Physician);
				createUser(c, "bah", "abcd", true, "a", "b", Nurse);
				//createUser(c, "foo", "1234", true, "a", "b", Reception);
				
				int cnt = 0;
				ResultSet r = query(c, "SELECT * FROM users");
				while(r.next())
				{
					new UserInfo(cnt++);
				}
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
	
	private static void createUser(Connection c, String uname, String pwd, boolean hash, String fname, String lname, Roles role)
			throws SQLException, RPMException
	{
		if(hash)
			pwd = Util.hash(pwd);
		try
		{
			update(c,
					"INSERT INTO users (uname, pwd, fname, lname, role) VALUES ('%s','%s','%s','%s','%s')".formatted(uname, pwd, fname, lname,
							role.name()));
		}
		catch(SQLException e)
		{
			if(e.getMessage().contains("UNIQUE"))
				throw new DuplicateUsernameException();
			throw e;
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
	
	public static boolean login(Connection c, String uname, String hashedPwd)
	{
		try
		{
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery("SELECT pwd FROM users WHERE uname = '" + uname + "'");
			if(!r.next()) return false;
			if(!hashedPwd.equals(r.getString(1))) return false;
			if(r.next()) throw new RPMError();
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
