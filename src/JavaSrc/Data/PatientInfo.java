package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchPatientException;
import JavaSrc.Exceptions.NoSuchUserException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientInfo
{
	public final int id;
	public String fname, mname, lname, phone, email, address, insuranceinfo, allergies,
			medications, heartrate, temperature, reasonforvisit, preexistingconditions,
			nursecomment, diagnosis, test, results, billingmethod;
	
	PatientInfo() {id = 0}
	
	PatientInfo(int patientID) throws NoSuchPatientException
	{
		try
		{
			ResultSet r = SQLHandler.fetchPatientRow(patientID);
			if(r == null || !r.next()) throw new NoSuchPatientException();
			id = r.getInt(1);
			load(r);
			if(id != patientID) throw new RPMError();
			System.out.println(this);
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
		fname = r.getString();
		mname = r.getString();
		lname = r.getString();
		phone = r.getString();
		email = r.getString();
		address = r.getString();
		insuranceinfo = r.getString();
		allergies = r.getString();
		medications = r.getString();
		heartrate = r.getString();
		temperature = r.getString();
		reasonforvisit = r.getString();
		preexistingconditions = r.getString();
		nursecomment = r.getString();
		diagnosis = r.getString();
		test = r.getString();
		results = r.getString();
		billingmethod = r.getString();
	}
}