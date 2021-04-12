package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class MedicalChartController implements SceneController
{
	public TextField physician;
	public TextField nameTextField;
	public TextField ageTextField;
	public TextField sex;
	public TextArea symptomsTextArea;
	public TextArea testsTextArea;
	public TextArea nurseComment;
	public Button editFieldButton;
	public Button testPatientButton;
	public Button cancelButton;
	public Button submitButton;
	
	private boolean busy = false;
	
	@FXML
	private Label error;
	
	public void doCancel(ActionEvent event)
	{
		//TODO create submit button
		if(busy) return;
		Client.singleton.runCommand("loadpat");
		busy = true;
	}
	
	public void doEdit(ActionEvent event)
	{
		//TODO create edit feature for fields
	}
	
	public void doTest(ActionEvent event)
	{
		//TODO create feature to perform a test
	}
	
	public void doSubmit(ActionEvent event)
	{
		//TODO create cancel feature to go back
		if(busy) return;
		if(physician.getText().isBlank() || nameTextField.getText().isBlank() || ageTextField.getText().isBlank() || sex.getText().isBlank() || symptomsTextArea.getText().isBlank() || testsTextArea.getText().isBlank())
		{
			error.setText("Missing required fields!");
			return;
		}
		try
		{
			Integer.parseInt(ageTextField.getText().trim());
		}
		catch(NumberFormatException e)
		{
			error.setText("Age must be a number");
			return;
		}
		Client.singleton.runCommand("createpat %s;%s;%s;%s;%s;%s;%s".formatted(nameTextField.getText().trim(), symptomsTextArea.getText().trim(),
		                                                                       testsTextArea.getText().trim(), ageTextField.getText().trim(),
		                                                                       sex.getText().trim(), physician.getText().trim(),
		                                                                       nurseComment.getText().trim()));
		busy = true;
	}
	
	public void fillTestsPerformed(ActionEvent event)
	{
		//TODO fills the tests performed field after button is pressed
	}
	
	@Override
	public void clear()
	{
		busy = false;
		physician.setText("");
		nameTextField.setText("");
		ageTextField.setText("");
		sex.setText("");
		symptomsTextArea.setText("");
		testsTextArea.setText("");
		nurseComment.setText("");
		error.setText("");
	}
	
	@Override
	public void error(String msg)
	{
		if(!msg.isBlank()) busy = false;
		error.setText(msg);
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
}
