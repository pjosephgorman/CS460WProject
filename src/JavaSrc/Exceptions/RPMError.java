package JavaSrc.Exceptions;

import JavaSrc.ErrorCodes;

public class RPMError extends RPMRuntimeException
{
	public RPMError()
	{
		super("Unknown Error");
	}
	public RPMError(ErrorCodes c, String msg, boolean isWarn)
	{
		super(c, msg);
		warn = isWarn;
	}
}
