package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import JavaSrc.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements SceneController
{
	public Button loginButton;
	public TextField uname;
	public PasswordField pwd;
	@FXML
	private Label error;
	
	
	public void doLogin(ActionEvent event)
	{
		if(uname.getText().equals("") || pwd.getText().equals(""))
		{
			error.setText("Missing required fields!");
			return;
		}
		error.setText("");
		Client.singleton.runCommand("login %s %s".formatted((uname.getText().replaceAll(" ", "")), Util.hash(pwd.getText().replaceAll(
				" ", ""))));
		Client.singleton.setMainMenu();
	}
	
	@Override
	public void clear()
	{
		uname.setText("");
		pwd.setText("");
		error.setText("");
	}
	
	@Override
	public void error(String msg)
	{
		error.setText(msg);
		if(!msg.equals(""))
		{
			//Clear the password field on a failed login
			pwd.setText("");
		}
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
}
