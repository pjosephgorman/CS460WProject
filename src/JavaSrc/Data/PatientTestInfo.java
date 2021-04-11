package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchPatientException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientTestInfo
{
	public final int id;
	public final int testId;
	public String testperformed, testresults;
	
	private PatientTestInfo(int testId, int patientId)
	{
		id = testId;
	}
	
	public static PatientTestInfo loadPatient(int patientId) throws NoSuchPatientException
	{
		try
		{
			ResultSet r = SQLHandler.fetchPatientRow(patientTestID);
			if(r == null || !r.next()) throw new NoSuchPatientException();
			if(patientTestID != r.getInt(1)) throw new RPMError();
			PatientTestInfo ret = new PatientTestInfo(patientTestID);
			ret.load(r);
			return ret;
		}
		catch(NoSuchPatientException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			Util.trace(e);
			throw new NoSuchPatientException();
		}
	}
	
	PatientTestInfo(ResultSet r) throws SQLException
	{
		id = r.getInt(1);
		load(r);
		System.out.println(this);
	}
	
	private void load(ResultSet r) throws SQLException
	{
	
	}
}