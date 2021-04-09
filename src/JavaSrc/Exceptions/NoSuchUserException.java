package JavaSrc.Exceptions;

public class NoSuchUserException extends RuntimeException
{
	public NoSuchUserException()
	{
		super("\nThe designated user could not be found!");
	}
}
