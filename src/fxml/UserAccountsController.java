package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import JavaSrc.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserAccountsController implements SceneController
{
	public TextField username;
	public PasswordField password;
	public PasswordField confirmPassword;
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
	
	
	public void doCancel(ActionEvent event)
	{
		//TODO create cancel feature to go back
		if(busy) return;
		Client.singleton.runCommand("loadacp");
		busy = true;
	}
	
	public void doSubmit(ActionEvent event)
	{
		//TODO create submit button that sends data to database
		if(busy) return;
		
		if(username.getText().isBlank() || password.getText().isBlank() || confirmPassword.getText().isBlank() || firstName.getText().isBlank() || lastName.getText().isBlank() || phoneNumber.getText().isBlank()
		   || emailAddress.getText().isBlank() || role.getValue() == null)
		{
			error.setText("Missing required fields!");
			return;
		}
		if(!password.getText().equals(confirmPassword.getText()))
		{
			error.setText("Passwords must match");
			return;
		}
		
		Client.singleton.runCommand(
				"createuser %s;%s;%s;%s;%s;%s;%s;%s".formatted(username.getText().replaceAll(" ", ""), Util.hash(password.getText().replaceAll(" ",
				                                                                                                                             "")),
				                                            firstName.getText().trim(), middleName.getText().trim(), lastName.getText().trim(),
				                                            phoneNumber.getText().trim(), emailAddress.getText().trim(), role.getValue().toString()));
		busy = true;
	}
	
	@Override
	public void clear()
	{
		busy = false;
		role.setValue(null);
		username.setText("");
		password.setText("");
		confirmPassword.setText("");
		firstName.setText("");
		middleName.setText("");
		lastName.setText("");
		phoneNumber.setText("");
		emailAddress.setText("");
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
	
	
	public void initialize(){
		for(Roles r : Roles.values()){
			role.getItems().add(r);
		}
	}
}
