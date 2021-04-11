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
							mname      VARCHAR(64),
							lname      VARCHAR(64)  NOT NULL,
							role       VARCHAR(20)  NOT NULL CHECK(role IN (%s)),
							phone      VARCHAR(20)  NOT NULL,
							email      VARCHAR(50)  NOT NULL
						);""".formatted(rolelist));
				
				update(c, """
						CREATE TABLE patients (
							patient_id    INT                     IDENTITY(0,1)   PRIMARY KEY,
							uname        VARCHAR(32)  NOT NULL                    UNIQUE,
							fname        VARCHAR(64)  NOT NULL,
							mname        VARCHAR(64),
							lname        VARCHAR(64)  NOT NULL,
							phone        VARCHAR(20)  NOT NULL,
							email        VARCHAR(50)  NOT NULL,
							address      VARCHAR(50)  NOT NULL,
							insurance    VARCHAR(50)  NOT NULL,
							allergies    VARCHAR(50),
							medications  VARCHAR(50),
							heartrate    VARCHAR(20),
							temperature  VARCHAR(50),
							reasonforvisit VARCHAR(50),
							preexistingconditions VARCHAR(50),
							nursecomment VARCHAR(100),
							diagnosis    VARCHAR(50),
							test         VARCHAR(32),
							results      VARCHAR(50),
							billingmethod VARCHAR(50)
						);""".formatted(rolelist));
				
				//Test values
				createUser(c, "foo", "pineapple", true, "a", "", "b", Admin, "555-555-5555", "foo@gmail.com");
				createUser(c, "bar", "banana", true, "a", "", "b", Physician, "555-555-5555", "foo@gmail.com");
				createUser(c, "bah", "abcd", true, "a", "", "b", Nurse, "555-555-5555", "foo@gmail.com");
				//createUser(c, "foo", "1234", true, "a", "", "b", Reception, "555-555-5555", "foo@gmail.com");
				
				int cnt = 0;
				ResultSet r = query(c, "SELECT * FROM users");
				while(r.next())
				{
					UserInfo info = UserInfo.loadUser(cnt++);
					String str = info.store();
					UserInfo.load(str);
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
	
	private static void createUser(Connection c, String uname, String pwd, boolean hash, String fname, String mname, String lname, Roles role,
	                               String phone, String email) throws SQLException, RPMException
	{
		if(hash) pwd = Util.hash(pwd);
		try
		{
			if(mname != null && !mname.equals(""))
			{
				update(c,
				       "INSERT INTO users (uname, pwd, fname, mname, lname, role, phone, email) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')".formatted(
						       uname, pwd, fname, mname, lname, role.name(), phone, email));
			}
			else
			{
				update(c,
				       "INSERT INTO users (uname, pwd, fname, lname, role, phone, email) VALUES ('%s','%s','%s','%s','%s','%s','%s')".formatted(uname,
				                                                                                                                                pwd,
				                                                                                                                                fname,
				                                                                                                                                lname,
				                                                                                                                                role.name(),
				                                                                                                                                phone,
				                                                                                                                                email));
			}
		}
		catch(SQLException e)
		{
			if(e.getMessage().contains("UNIQUE")) throw new DuplicateUsernameException();
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
	
	public static UserInfo loginUnhashed(Connection c, String uname, String rawPwd)
	{
		return login(c, uname, Util.hash(rawPwd));
	}
	
	public static UserInfo login(Connection c, String uname, String hashedPwd)
	{
		try
		{
			Statement s = c.createStatement();
			ResultSet r = s.executeQuery("SELECT pwd FROM users WHERE uname = '" + uname + "'");
			if(!r.next()) return null;
			if(!hashedPwd.equals(r.getString(1))) return null;
			if(r.next()) throw new RPMError();
			s = c.createStatement();
			r = s.executeQuery("SELECT user_id FROM users WHERE uname = '" + uname + "'");
			if(!r.next()) throw new RPMError();
			return UserInfo.loadUser(r.getInt(1));
		}
		catch(SQLException ignored)
		{
			return null;
		}
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
	
	static ResultSet fetchPatientRow(int PatientID)
	{
		try
		{
			Connection c = connect();
			return query(c, """
					SELECT * FROM patients WHERE patient_id = %d""".formatted(PatientID));
		}
		catch(SQLException ignored)
		{
			return null;
		}
	}
	
	public static int countUsers() throws SQLException
	{
		int count = 0;
		Connection c = connect();
		try
		{
			ResultSet r = query(c, "SELECT * FROM users");
			while(r.next()) ++count;
		}
		catch(Exception e)
		{
			Util.trace(e);
		}
		return count;
	}
}
