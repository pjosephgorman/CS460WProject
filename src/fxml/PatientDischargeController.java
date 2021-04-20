package fxml;

import JavaSrc.Client;
import JavaSrc.Data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PatientDischargeController implements SceneController
{
	public Label patientName;
	public TextField days;
	public TextField email;
	public ChoiceBox<Diagnosis> diagnosis;
	public TextArea instructions;
	public ChoiceBox<Medication> medications;
	public Button printBillButton;
	public Button cancelButton;
	
	@FXML
	private Label error;
	
	private String tests;
	
	private PatientInfo patInfo;
	
	private boolean busy = false;
	
	public void cancel(ActionEvent event){
		if(busy) return;
		Client.singleton.runCommand("loadpat");
		busy = true;
	}
	
	public void printBill(ActionEvent event){
		//TODO print itemized bill
		if(busy) return;
		if(days.getText().isBlank() || diagnosis.getValue() == null || medications.getValue() == null)
		{
			error.setText("Missing Required Fields");
			return;
		}
		Client.singleton.setBill(new String[] {patInfo.name, days.getText(), medications.getValue().toString(), tests, diagnosis.getValue().toString()});
		busy = true;
	}
	
	
	@Override
	public void clear()
	{
		busy = false;
		patientName.setText("");
		days.setText("");
		diagnosis.setValue(null);
		email.setText("");
		instructions.setText("");
		medications.setValue(null);
		patInfo = null;
	}
	
	@Override
	public void error(String msg)
	{
		if(!msg.isBlank()) busy = false;
		busy = false;
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
		printBillButton.setVisible(info != null && (info.role == Roles.Admin || info.role == Roles.Billing));
	}
	
	public void load(PatientInfo info)
	{
		patInfo = info;
		patientName.setText(info.name);
		tests = info.test;
		
	}
	
	public void initialize(){
		for(Diagnosis d : Diagnosis.values()){
			diagnosis.getItems().add(d);
		}
		for(Medication m : Medication.values()){
			medications.getItems().add(m);
		}
	}
}
