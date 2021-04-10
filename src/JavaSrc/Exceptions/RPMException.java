package JavaSrc.Exceptions;

import JavaSrc.ErrorCodes;

public class RPMException extends Exception implements ErrorCodable
{
	private ErrorCodes code;
	public boolean warn;
	public RPMException(String msg)
	{
		super(msg);
		code = ErrorCodes.UNKNOWN_ERROR;
	}
	public RPMException(ErrorCodes c, String msg)
	{
		super(msg);
		code = c;
	}
	
	@Override
	public ErrorCodes getCode()
	{
		return code;
	}
	
	@Override
	public boolean getWarn()
	{
		return warn;
	}
}
