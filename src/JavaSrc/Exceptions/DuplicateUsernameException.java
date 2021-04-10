package JavaSrc.Exceptions;

import JavaSrc.ErrorCodes;

public class DuplicateUsernameException extends RPMException
{
	public DuplicateUsernameException()
	{
		super(ErrorCodes.DUPE_UNAME, "Username already in use!");
	}
}
