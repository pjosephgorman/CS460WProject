package fxml;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class EditAccountController implements SceneController
{
	public TextField username;
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
	
	@Override
	public void clear()
	{
	
	}
	
	@Override
	public void error(String msg)
	{
	
	}
}
