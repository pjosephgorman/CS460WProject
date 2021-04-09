package fxml;

import JavaSrc.Client;
import JavaSrc.Util;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController
{
	public TextField uname;
	public PasswordField pwd;
	//TODO add 'Text'/'Label' for errors
	
	public void doLogin(ActionEvent event)
	{
		if(uname.getText().equals("") || pwd.getText().equals(""))
		{
			//TODO display error message 'missing required fields'
		}
		else Client.singleton.runCommand("login " + (uname.getText().replaceAll(" ", "")) + " " + Util.hash(pwd.getText().replaceAll(" ", "")));
	}
	
	public void clear()
	{
		uname.setText("");
		pwd.setText("");
	}
}
