package JavaSrc.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDischargeInfo
{
	public final int id;
	public int days;
	public String address, email, instructions;
	public Diagnosis diagnosis;
	public Medication medication;
	
	private PatientDischargeInfo(int disId, int patientId) { id = disId; }
	
	PatientDischargeInfo(ResultSet r) throws SQLException
	{
		id = r.getInt(1);
		load(r);
		System.out.println(this);
	}
	
	public double calculateBill(double days)
	{
		double medPrice = 10 + medication.ordinal() + 1;
		double pricePerDay = days * 100;
		return 0;
	}
	
	private void load(ResultSet r) throws SQLException
	{
		throw new SQLException();
	}
}