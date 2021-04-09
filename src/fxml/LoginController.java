package fxml;

import JavaSrc.Client;
import JavaSrc.Util;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController
{
	public TextField uname;
	public PasswordField pwd;
	public Label error;
	
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
	
	public void clear()
	{
		uname.setText("");
		pwd.setText("");
		error.setText("");
	}
}
