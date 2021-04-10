package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController implements SceneController
{
	public Button patientActionsButton;
	public Button acpButton;
	public Button logoutButton;
	
	public void doLogout(ActionEvent event) throws IOException
	{
		//TODO create logout function
		Client.singleton.logout();
	}
	
	public void goToACP(ActionEvent event)
	{
		//TODO create action to get to ACP Window
	}
	
	public void goToPatientActions(ActionEvent event){
		//TODO create actions to get to patient actions window
	}
	
	@Override
	public void clear()
	{
	
	}
	
	@Override
	public void error(String msg)
	{
	
	}
}
