package JavaSrc.Data;

import JavaSrc.Exceptions.NoSuchPatientException;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientInfo
{
	public final int id;
	public int age;
	public String name, symptoms, test, sex, physician, nursecomment;
	
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
	}
	
	
	public String store()
	{
		return id + ";" + name + ";" + symptoms + ";" + age + ";" + sex + ";" + (test == null ? "" : test) + ";" + (physician == null ? "" : physician) +
		       ";" + (nursecomment == null ? "" : nursecomment);
	}
	
	public static PatientInfo load(String str)
	{
		String[] args = str.split(";");
		PatientInfo ret = new PatientInfo(Integer.parseInt(args[0]));
		ret.name = args[1];
		ret.symptoms = args[2];
		ret.age = Integer.parseInt(args[3]);
		ret.sex = args[4];
		if(args.length < 6 || args[5].equals(""))
			ret.test = null;
		else ret.test = args[5];
		if(args.length < 7 || args[6].equals(""))
			ret.physician = null;
		else ret.physician = args[6];
		if(args.length < 8 || args[7].equals(""))
			ret.nursecomment = null;
		else ret.nursecomment = args[7];
		//System.out.printf("Loaded '%s' as:\n%s%n", str,ret);
		return ret;
	}
	
	private void load(ResultSet r) throws SQLException
	{
		name = r.getString(2);
		symptoms = r.getString(3);
		test = r.getString(4);
		age = r.getInt(5);
		sex = r.getString(6);
		physician = r.getString(7);
		nursecomment = r.getString(8);
	}
	
	@Override
	public String toString()
	{
		return "PatientInfo{" +
		       "id=" + id +
		       ", age=" + age +
		       ", name='" + name + '\'' +
		       ", symptoms='" + symptoms + '\'' +
		       ", test='" + test + '\'' +
		       ", sex='" + sex + '\'' +
		       ", physician='" + physician + '\'' +
		       ", nursecomment='" + nursecomment + '\'' +
		       '}';
	}
}
