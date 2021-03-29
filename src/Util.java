public class Util
{
	private static final boolean DEBUG = true;
	
	public static String hashPwd(String pwd)
	{
		return pwd; //Temporary, add hashing later
	}
	
	public static void error(String err) {if(DEBUG) System.err.println(err);}
}