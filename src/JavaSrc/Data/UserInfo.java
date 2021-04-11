package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchUserException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo
{
	public final int id;
	public String uname, pwd, fname, mname, lname, phone, email;
	public Roles role;
	
	private UserInfo(int userID)
	{
		id = userID;
	}
	
	public static UserInfo loadUser(int userID) throws NoSuchUserException
	{
		try
		{
			ResultSet r = SQLHandler.fetchUserRow(userID);
			if(r == null || !r.next()) throw new NoSuchUserException();
			if(userID != r.getInt(1)) throw new RPMError();
			UserInfo ret = new UserInfo(userID);
			ret.load(r);
			return ret;
		}
		catch(NoSuchUserException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			Util.trace(e);
			throw new NoSuchUserException();
		}
	}
	
	UserInfo(ResultSet r) throws SQLException
	{
		id = r.getInt(1);
		load(r);
		System.out.println(this);
	}
	
	private void load(ResultSet r) throws SQLException
	{
		uname = r.getString(2);
		pwd = r.getString(3);
		fname = r.getString(4);
		mname = r.getString(5);
		lname = r.getString(6);
		role = Roles.valueOf(r.getString(7));
		phone = r.getString(8);
		email = r.getString(9);
	}
	
	public String store()
	{
		return id + ";" + uname + ";" + pwd + ";" + fname + ";" + (mname == null ? "" : mname) + ";" + lname + ";" + role + ";" + phone + ";" + email;
	}
	
	public static UserInfo load(String str)
	{
		String[] args = str.split(";");
		UserInfo ret = new UserInfo(Integer.parseInt(args[0]));
		ret.uname = args[1];
		ret.pwd = args[2];
		ret.fname = args[3];
		if(args[4].equals(""))
			ret.mname = null;
		else ret.mname = args[4];
		ret.lname = args[5];
		ret.role = Roles.valueOf(args[6]);
		ret.phone = args[7];
		ret.email = args[8];
		//System.out.printf("Loaded '%s' as:\n%s%n", str,ret);
		return ret;
	}
	
	@Override
	public String toString()
	{
		return "UserInfo{" +
		       "id=" + id +
		       ", uname='" + uname + '\'' +
		       ", fname='" + fname + '\'' +
		       (mname == null
		            ? ""
		            : ", mname='" + mname + '\'') +
		       ", lname='" + lname + '\'' +
		       ", phone='" + phone + '\'' +
		       ", email='" + email + '\'' +
		       ", role=" + role +
		       ", pwd='" + Util.abbrev(pwd) + '\'' +
		       '}';
	}
}
