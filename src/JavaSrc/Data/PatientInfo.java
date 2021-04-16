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
	public String name, symptoms, test = "", sex, physician = "", nursecomment = "", vitals = "", height = "", weight = "";
	
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
	
	PatientInfo(ResultSet r) throws SQLException
	{
		id = r.getInt(1);
		load(r);
	}
	
	
	public String store()
	{
		return id + ";" + name + ";" + symptoms + ";" + age + ";" + sex + ";" + height + ";" + weight +
		       ";" + vitals + ";" + (test == null ? "" : test) + ";" + (physician == null ? "" : physician)
		       + ";" + (nursecomment == null ? "" : nursecomment);
	}
	
	public static PatientInfo load(String str)
	{
		String[] args = str.split(";", -1);
		PatientInfo ret = new PatientInfo(Integer.parseInt(args[0]));
		ret.name = args[1];
		ret.symptoms = args[2];
		ret.age = Integer.parseInt(args[3]);
		ret.sex = args[4];
		if(args.length < 6 || args[5].equals("")) ret.height = null;
		else ret.height = "" + Integer.parseInt(args[5]);
		if(args.length < 7 || args[6].equals("")) ret.weight = null;
		else ret.weight = "" + Integer.parseInt(args[6]);
		if(args.length < 8 || args[7].equals("")) ret.vitals = null;
		else ret.vitals = args[7];
		if(args.length < 9 || args[8].equals("")) ret.test = null;
		else ret.test = args[8];
		if(args.length < 10 || args[9].equals("")) ret.physician = null;
		else ret.physician = args[9];
		if(args.length < 11 || args[10].equals("")) ret.nursecomment = null;
		else ret.nursecomment = args[10];
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
		height = "" + r.getInt(7);
		weight = "" + r.getInt(8);
		vitals = r.getString(9);
		physician = r.getString(10);
		nursecomment = r.getString(11);
	}
	
	/*@Override
	public String toString()
	{
		return "PatientInfo{" + "id=" + id + ", age=" + age + ", name='" + name + '\'' + ", symptoms='" + symptoms + '\'' + ", test='" + test + '\'' + ", sex='" + sex + '\'' + ", height=" + height + ", weight=" + weight + ", vitals='" + vitals + '\'' + ", physician='" + physician + '\'' + ", nursecomment='" + nursecomment + '\'' + '}';
	}*/
	
	@Override
	public String toString()
	{
		return "PatientInfo{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", age=" + age +
		       ", sex='" + sex + '\'' +
		       ", height=" + height +
		       ", weight=" + weight +
		       ", symptoms='" + symptoms + '\'' +
		       (physician == null
		        ? ""
		        : ", physician='" + physician + '\'') +
		       (vitals == null
		        ? ""
		        : ", vitals='" + vitals + '\'') +
		       (nursecomment == null
		        ? ""
		        : ", nursecomment='" + nursecomment + '\'') +
		       (test == null
		        ? ""
		        : ", test='" + test + '\'') +
		       '}';
	}
}
