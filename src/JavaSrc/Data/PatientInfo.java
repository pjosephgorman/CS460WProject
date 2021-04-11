package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchPatientException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientInfo
{
	public final int id;
	public String fname, mname, lname, phone, email, address, insurance, allergies,
			medications, heartrate, temperature, reasonforvisit, preexistingconditions,
			nursecomment, diagnosis, test, results, billingmethod;
	
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
		fname = r.getString(3);
		mname = r.getString(4);
		lname = r.getString(5);
		phone = r.getString(6);
		email = r.getString(7);
		address = r.getString(8);
		insurance = r.getString(9);
		allergies = r.getString(10);
		medications = r.getString(11);
		heartrate = r.getString(12);
		temperature = r.getString(13);
		reasonforvisit = r.getString(14);
		preexistingconditions = r.getString(15);
		nursecomment = r.getString(16);
		diagnosis = r.getString(17);
		test = r.getString(18);
		results = r.getString(19);
		billingmethod = r.getString(20);
	}
}