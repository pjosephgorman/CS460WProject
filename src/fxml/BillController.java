package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

public class BillController implements SceneController
{
	public TextArea itemizedBill;
	
	private boolean busy = false;
	
	public void finish(ActionEvent event)
	{
		if(busy) return;
		Client.singleton.runCommand("loadpat");
		busy = true;
	}

	public void createBill(String[] args)
	{
		double pricePerDay = Integer.parseInt(args[1]) * 1200;
		double totalCost = pricePerDay + medicationsPrescribed(args[2]) + testsPerformed(args[3]);
		
		itemizedBill.setText("Patient Name: " + args[0] + "\t\t\t\t\tDiagnosis: " + args[4]
		                     + "\n\n\nPatient Address: " + args[6]
		                     + "\n\n\nInsurance: " + args[5]
		                     + "\n\n\nDays ($1200/day) $"
		                     + pricePerDay + "\n\n\nMedication Prescribed: " + args[2] +
		                     " $"+ medicationsPrescribed(args[2]) + "\n\n\nTest Performed: " + args[3] + " $"  +  testsPerformed(args[3])
		                     + "\n\n\nTotal Bill: $" + totalCost);
		System.out.println(args[0]);
	}
	
	public int medicationsPrescribed(String args)
	{
		return switch(args)
				{
					case "Corticosteroids" -> 300;
					case "IVInsulin" -> 400;
					case "AntiBiotics" -> 150;
					case "Steroids" -> 250;
					case "AntiViral" -> 200;
					default -> 0;
				};
	}
	
	public int testsPerformed(String args)
	{
		return switch(args)
				{
					case "MRI" -> 2000;
					case "CT" -> 1500;
					case "X-Ray" -> 1200;
					case "Red Blood Cell" -> 700;
					case "White Blood Cell" -> 700;
					case "Liver Function" -> 800;
					case "Kidney Function" -> 800;
					case "Electrolyte Test" -> 200;
					case "Urinary Test" -> 150;
					case "Stool Test" -> 100;
					default -> 0;
				};
	}
	
	@Override
	public void clear()
	{
		busy = false;
		itemizedBill.setText("");
	}
	
	@Override
	public void error(String msg)
	{
	
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
}
