package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ViewAcpAccountController implements SceneController
{
	public TextField username;
	public PasswordField password;
	public TextField firstName;
	public TextField middleName;
	public TextField lastName;
	public ChoiceBox<Roles> role;
	public TextField phoneNumber;
	public TextField emailAddress;
	public Button cancelButton;
	public Button submitButton;
	
	private boolean busy = false;
	
	public void view(String[] args)
	{
		username.setText(args[0]);
		password.setText(args[1]);
		firstName.setText(args[2]);
		middleName.setText(args[3]);
		lastName.setText(args[4]);
		role.setValue(Roles.valueOf(args[5]));
		phoneNumber.setText(args[6]);
		emailAddress.setText(args[7]);
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
		busy = true;
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
