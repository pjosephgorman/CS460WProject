package JavaSrc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util
{
	private static final boolean DEBUG = true;
	
	public static String hash(String str)
	{
		try
		{
			StringBuilder hashed = new StringBuilder(new BigInteger(1, MessageDigest.getInstance("SHA-512").digest(str.getBytes())).toString(16));
			while(hashed.length() < 128) //Insert leading 0's
				hashed.insert(0, "0");
			//hashed.insert(0, "0x");
			return hashed.toString();
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static void error(String err) {if(DEBUG) System.err.println(err);}
	public static void msg(String msg) {if(DEBUG) System.out.println(msg);}
	public static void trace(Exception e) {if(DEBUG) e.printStackTrace();}
	
	public static String abbrev(String str)
	{
		if(str.length() < 13) return str;
		return str.substring(0,10) + "...";
	}
}
