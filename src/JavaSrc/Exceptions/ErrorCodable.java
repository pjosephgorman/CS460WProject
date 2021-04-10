package JavaSrc.Exceptions;

import JavaSrc.ErrorCodes;

public interface ErrorCodable
{
	ErrorCodes getCode();
	boolean getWarn();
}
