package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ViewMedicalChartController implements SceneController
{
	public TextField physician;
	public TextField nameTextField;
	public TextField ageTextField;
	public TextField sex;
	public TextArea symptomsTextArea;
	public TextArea testsTextArea;
	public TextArea nurseComment;
	public TextField height;
	public TextField weight;
	public TextArea vitals;
	public Button cancelButton;
	public Button submitButton;
	
	private boolean busy = false;
	private boolean doctor = false;
	
	public void view(String[] args)
	{
		if(!doctor)
		{
			physician.setText(args[0]);
			physician.setEditable(false);
			nameTextField.setText(args[1]);
			nameTextField.setEditable(false);
			ageTextField.setText(args[2]);
			ageTextField.setEditable(false);
			height.setText(args[3]);
			height.setEditable(false);
			sex.setText(args[4]);
			sex.setEditable(false);
			weight.setText(args[5]);
			weight.setEditable(false);
			symptomsTextArea.setText(args[6]);
			symptomsTextArea.setEditable(false);
			vitals.setText(args[7]);
			vitals.setEditable(false);
			testsTextArea.setText(args[8]);
			testsTextArea.setEditable(false);
			nurseComment.setText(args[9]);
			nurseComment.setEditable(false);
		}
		else
		{
			physician.setText(args[0]);
			nameTextField.setText(args[1]);
			ageTextField.setText(args[2]);
			height.setText(args[3]);
			sex.setText(args[4]);
			weight.setText(args[5]);
			symptomsTextArea.setText(args[6]);
			vitals.setText(args[7]);
			testsTextArea.setText(args[8]);
			nurseComment.setText(args[9]);
		}
	}
	
	public void doSubmit(ActionEvent event)
	{
		if(busy) return;
		busy = true;
		Client.singleton.runCommand("loadpat");
	}
	
	public void doCancel(ActionEvent event)
	{
		if(busy) return;
		busy = true;
		Client.singleton.runCommand("loadpat");
	}
	
	@Override
	public void clear()
	{
		busy = false;
	}
	
	@Override
	public void error(String msg)
	{
	
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
		doctor = info.role == Roles.Physician;
	}
	
	
}
