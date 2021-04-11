package fxml;

import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class EditAccountController implements SceneController
{
	public TextField username;
	public TextField oldpassword;
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
	
	public void doCancel(ActionEvent event){
		//TODO create cancel feature to go back
	}
	
	public void doSubmit(ActionEvent event){
		//TODO create submit button that sends data to database
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
