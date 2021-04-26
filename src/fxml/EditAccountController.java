package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import JavaSrc.Util;
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
	public ChoiceBox<Roles> role;
	public TextField phoneNumber;
	public TextField emailAddress;
	public Button cancelButton;
	public Button submitButton;
	private boolean busy = false;
	
	@FXML
	private Label error;
	
	public void doCancel(ActionEvent event){
		clear();
		Client.singleton.setMainMenu();
	}
	
	public void doSubmit(ActionEvent event){
		//TODO create submit button that sends data to database
		if(busy) return;
		if(oldPassword.getText().isBlank() || newPassword.getText().isBlank() || confirmPassword.getText().isBlank()
				|| phoneNumber.getText().isBlank() || emailAddress.getText().isBlank())
		{
			error.setText("Missing required fields!");
			return;
		}
		if(!newPassword.getText().equals(confirmPassword.getText()))
		{
			error.setText("Passwords must match");
		}
		if(oldPassword.getText().equals(confirmPassword.getText()))
		{
			error.setText("Can't use pre-existing password");
			return;
		}
		Client.singleton.runCommand("edituser %s;%s;;%s;%s".formatted(oldPassword.getText().replaceAll(" ", ""),
		                                                                Util.hash(newPassword.getText().replaceAll(" ", "")),
		                                                                phoneNumber.getText().trim(), emailAddress.getText().trim()));
		busy = true;
	}
	
	
	@Override
	public void clear()
	{
		busy = false;
		oldPassword.setText("");
		newPassword.setText("");
		confirmPassword.setText("");
		phoneNumber.setText("");
		emailAddress.setText("");
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
