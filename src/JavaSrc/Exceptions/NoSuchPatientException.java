package JavaSrc.Exceptions;

public class NoSuchPatientException extends RPMRuntimeException
{
	public NoSuchPatientException() {super("\nThe designated patient could not be found!");}
}
