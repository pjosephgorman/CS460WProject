package JavaSrc.Exceptions;

public class DuplicateUsernameException extends RuntimeException
{
	public DuplicateUsernameException()
	{
		super("\nMultiple entries found in the database with duplicate usernames!\nThese rows must be fixed manually!");
	}
}
