package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Diagnosis;
import JavaSrc.Data.Medication;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PatientDischargeController implements SceneController
{
	public TextField patientName;
	public TextField days;
	public TextField email;
	public ChoiceBox<Diagnosis> diagnosis;
	public TextArea instructions;
	public ChoiceBox<Medication> medications;
	public Button submitButton;
	public Button cancelButton;
	
	private boolean busy = false;
	
	public void cancel(ActionEvent event){
		if(busy) return;
		Client.singleton.runCommand("loadpat");
		busy = true;
	}
	
	public void submit(ActionEvent event){
		//TODO submit discharge information
	}
	
	public void printBill(ActionEvent event){
		//TODO print itemized bill
	}
	
	
	@Override
	public void clear()
	{
	
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
