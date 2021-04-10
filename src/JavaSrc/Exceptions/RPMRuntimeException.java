package JavaSrc.Exceptions;

import JavaSrc.ErrorCodes;

public class RPMRuntimeException extends RuntimeException implements ErrorCodable
{
	private ErrorCodes code;
	public boolean warn;
	
	public RPMRuntimeException(String msg)
	{
		super(msg);
		code = ErrorCodes.UNKNOWN_ERROR;
	}
	
	public RPMRuntimeException(ErrorCodes c, String msg)
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
