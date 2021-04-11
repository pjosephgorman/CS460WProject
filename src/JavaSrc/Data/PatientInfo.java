package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchPatientException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientInfo
{
	public final int id;
	public String name, symptoms, test, age, sex, physician, nursecomment;
	
	//correct String list
	/*name symptom */
	
	private PatientInfo(int patientID)
	{
		id = patientID;
	}
	
	public static PatientInfo loadPatient(int patientID) throws NoSuchPatientException
	{
		try
		{
			ResultSet r = SQLHandler.fetchPatientRow(patientID);
			if(r == null || !r.next()) throw new NoSuchPatientException();
			if(patientID != r.getInt(1)) throw new RPMError();
			PatientInfo ret = new PatientInfo(patientID);
			ret.load(r);
			return ret;
		}
		catch (NoSuchPatientException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			Util.trace(e);
			throw new NoSuchPatientException();
		}
	}
	
	PatientInfo(ResultSet r) throws SQLException
	{
		id = r.getInt(1);
		load(r);
		System.out.println(this);
	}
	
	private void load(ResultSet r) throws SQLException
	{
		name = r.getString(3);
		symptoms = r.getString(4);
		test = r.getString(5);
		age = r.getString(6);
		sex = r.getString(7);
		test = r.getString(8);
		physician = r.getString(9);
		nursecomment = r.getString(10);
	}
}