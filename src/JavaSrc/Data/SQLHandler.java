package JavaSrc.Data;

import JavaSrc.Exceptions.DuplicateUsernameException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Exceptions.RPMException;
import JavaSrc.Util;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;
import java.util.ArrayList;

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
				if(!query(c, "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users'").next())
				{
					
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
					
					//Test values
					createUser(c, "admin", "admin", true, "Administrator", "", "Admin", Admin, "555-555-5555", "foo@gmail.com");
					createUser(c, "billing", "bill", true, "a", "", "b", Billing, "555-555-5555", "foo@gmail.com");
					createUser(c, "physician", "doc", true, "a", "", "b", Physician, "555-555-5555", "foo@gmail.com");
					createUser(c, "nurse", "nur", true, "a", "", "b", Nurse, "555-555-5555", "foo@gmail.com");
				}
				if(!query(c, "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'patients'").next())
				{
					update(c, """
					          CREATE TABLE patients (
					          	patient_id   INT                     IDENTITY(0,1)   PRIMARY KEY,
					          	name         VARCHAR(64)  NOT NULL,
					          	symptoms     VARCHAR(64)  NOT NULL,
					          	test         VARCHAR(64),
					          	age          INT          NOT NULL,
					          	sex          VARCHAR(50)  NOT NULL,
					          	height       INT,
					          	weight       INT,
					          	vitals       VARCHAR(112),
					          	physician    VARCHAR(32),
					          	nursecomment VARCHAR(112),
					          );""");
					createPatient(c, "John Smith", "Sore Throat", "MRI", 30, "Male", 60, 250, "97 degrees, 150/60 BP", null, null);
					createPatient(c, "Jane Doe", "Stomach Ache", "MRI", 25, "Female", 55, 200, "99 degrees, 140/55 BP", null, null);
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
	
	public static void createUser(String[] args) throws SQLException, RPMException
	{
		Connection c = connect();
		createUser(c, args[0], args[1], false, args[2], args[3], args[4], Roles.valueOf(args[7]), args[5], args[6]);
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
						"INSERT INTO users (uname, pwd, fname, lname, role, phone, email) VALUES ('%s','%s','%s','%s','%s','%s','%s')".formatted(
								uname,
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
	
	public static void createPatient(String[] args) throws SQLException, RPMException
	{
		Connection c = connect();
		createPatient(c, args[0], args[1], args[2], Integer.parseInt(args[3]), args[4], Integer.parseInt(args[5]),
				Integer.parseInt(args[6]),
				args[7], args[8], args[9]);
	}
	
	private static void createPatient(Connection c, String name, String symptoms, String test, int age, String sex, int height, int weight,
	                                  String vitals, String physician, String nursecomment) throws SQLException
	{
		try
		{
			String[] nullables = buildNullable(new String[]{test, physician, nursecomment, ""+height, ""+weight, vitals}, new String[]{"test",
					"physician"
					, "nursecomment", "height", "weight", "vitals"});
			
			//Also the table patient table isn't showing any content when we run the Client
			update(c, "INSERT INTO patients (name, symptoms, age, sex%s) VALUES ('%s','%s','%d','%s'%s)".formatted(nullables[0],
					name,
					symptoms,
					age, sex, nullables[1]));
		}
		catch(SQLException e)
		{
			if(e.getMessage().contains("UNIQUE")) throw new RPMError();
			throw e;
		}
	}
	
	private static String[] buildNullable(String[] val, String[] key)
	{
		int len = Math.min(key.length, val.length);
		StringBuilder[] ret = new StringBuilder[]{new StringBuilder(), new StringBuilder()};
		for(int q = 0; q < len; ++q)
		{
			if(val[q] == null || val[q].isBlank())
				continue;
			ret[0].append(", ").append(key[q]);
			ret[1].append(", '").append(val[q]).append('\'');
		}
		return new String[]{ret[0].toString(), ret[1].toString()};
	}
	
	private static void clear()
	{
		try
		{
			Connection c = connect();
			update(c, "DROP TABLE IF EXISTS users");
			update(c, "DROP TABLE IF EXISTS patients");
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
	
	public static void delUser(int UserID)
	{
		try
		{
			Connection c = connect();
			update(c, """
			          DELETE FROM users WHERE user_id = %d""".formatted(UserID));
		}
		catch(SQLException ignored) {}
	}
	
	public static void delPatient(int PatientID)
	{
		try
		{
			Connection c = connect();
			update(c, """
			          DELETE FROM patients WHERE patient_id = %d""".formatted(PatientID));
		}
		catch(SQLException ignored) {}
	}
	
	public static ArrayList<UserInfo> loadAllUserInfos()
	{
		ArrayList<UserInfo> list = new ArrayList<>();
		try
		{
			Connection c = connect();
			ResultSet r = query(c, "SELECT * FROM users");
			while(r.next())
			{
				try
				{
					list.add(new UserInfo(r));
				}
				catch(Exception ignored) {}
			}
		}
		catch(SQLException ignored) {}
		return list;
	}
	
	public static ArrayList<PatientInfo> loadAllPatientInfos()
	{
		ArrayList<PatientInfo> list = new ArrayList<>();
		try
		{
			Connection c = connect();
			ResultSet r = query(c, "SELECT * FROM patients");
			while(r.next())
			{
				try
				{
					list.add(new PatientInfo(r));
				}
				catch(Exception ignored) {}
			}
		}
		catch(SQLException ignored) {}
		return list;
	}
}
