package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditAccountController implements SceneController
{
	public TextField username;
	public TextField oldPassword;
	public TextField newPassword;
	public TextField confirmPassword;
	public TextField firstName;
	public TextField middleName;
	public TextField lastName;
	public ChoiceBox role;
	public TextField phoneNumber;
	public TextField emailAddress;
	public Button cancelButton;
	public Button submitButton;
	
	@FXML
	private Label error;
	
	public void doCancel(ActionEvent event){
		clear();
		Client.singleton.setMainMenu();
	}
	
	public void doSubmit(ActionEvent event){
		//TODO create submit button that sends data to database
		if(oldPassword.getText().isBlank() || newPassword.getText().isBlank() || confirmPassword.getText().isBlank()
				|| phoneNumber.getText().isBlank() || emailAddress.getText().isBlank())
		{
			error.setText("Missing required fields!");
			return;
		}
	}
	
	
	@Override
	public void clear()
	{
		oldPassword.setText("");
		newPassword.setText("");
		confirmPassword.setText("");
		phoneNumber.setText("");
		emailAddress.setText("");
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
