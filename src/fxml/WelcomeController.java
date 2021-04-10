package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WelcomeController
{
	public Button login;
	public Label welcome;
	
	public void goToLogin(ActionEvent event){
		Client.singleton.setLogin();
	}
}
