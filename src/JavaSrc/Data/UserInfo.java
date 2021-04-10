package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchUserException;
import JavaSrc.Util;

import java.sql.ResultSet;

public class UserInfo
{
	public final int id;
	public String uname, pwd, fname, mname, lname, phone, email;
	public Roles role;
	
	UserInfo()
	{
		id = 0;
	}
	
	UserInfo(int userID) throws NoSuchUserException
	{
		try
		{
			id = userID;
			ResultSet r = SQLHandler.fetchUserRow(userID);
			if(r == null || !r.next()) throw new NoSuchUserException();
			uname = r.getString(2);
			pwd = r.getString(3);
			fname = r.getString(4);
			mname = r.getString(5);
			lname = r.getString(6);
			role = Roles.valueOf(r.getString(7));
			phone = r.getString(8);
			email = r.getString(9);
			System.out.println(this);
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
	
	@Override
	public String toString()
	{
		return "UserInfo{" +
		       "id=" + id +
		       ", uname='" + uname + '\'' +
		       ", fname='" + fname + '\'' +
		       ", mname='" + mname + '\'' +
		       ", lname='" + lname + '\'' +
		       ", phone='" + phone + '\'' +
		       ", email='" + email + '\'' +
		       ", role=" + role +
		       ", pwd='" + Util.abbrev(pwd) + '\'' +
		       '}';
	}
}
