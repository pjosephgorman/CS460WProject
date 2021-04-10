package JavaSrc.Exceptions;

public class NoSuchUserException extends RPMRuntimeException
{
	public NoSuchUserException()
	{
		super("\nThe designated user could not be found!");
	}
}
