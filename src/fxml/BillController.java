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
		if(busy) return;
		double pricePerDay = Integer.parseInt(args[1]) * 1200;
		double totalCost = pricePerDay + medicationsPrescribed(args[2]) + testsPerformed(args[3]);
		
		itemizedBill.setText("Patient Name: " + args[0] + "\t\t\t\t\tDiagnosis: " + args[4] + "\n\n\nDays ($1200/day) $"
		                     + pricePerDay + "\n\n\nMedication Prescribed: " + args[2] +
		                     " $"+ medicationsPrescribed(args[2]) + "\n\n\nTest Performed: " + args[3] + " $"  +  testsPerformed(args[3])
		                     + "\n\n\nTotal Bill: $" + totalCost);
		System.out.println(args[0]);
		busy = true;
	}
	
	public int medicationsPrescribed(String args)
	{
	 	switch (args)
		{
			case "Corticosteroids":
				return 300;
			case "IVInsulin":
				return 400;
			case "AntiBiotics":
				return 150;
			case "Steroids":
				return 250;
			case "AntiViral":
				return 200;
			default:
				return 0;
		}
	}
	
	public int testsPerformed(String args)
	{
		switch (args)
		{
			case "MRI":
				return 2000;
			case "CT":
				return 1500;
			case "X-Ray":
				return 1200;
			case "Red Blood Cell":
				return 700;
			case "White Blood Cell":
				return 700;
			case "Liver Function":
				return 800;
			case "Kidney Function":
				return 800;
			case "Electrolyte Test":
				return 200;
			case "Urinary Test":
				return 150;
			case "Stool Test":
				return 100;
			default:
				return 0;
		}
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
