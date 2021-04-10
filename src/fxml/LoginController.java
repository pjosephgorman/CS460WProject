package fxml;

import JavaSrc.Client;
import JavaSrc.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements SceneController
{
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
		Client.singleton.runCommand("login " + (uname.getText().replaceAll(" ", "")) + " " + Util.hash(pwd.getText().replaceAll(" ", "")));
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
}
