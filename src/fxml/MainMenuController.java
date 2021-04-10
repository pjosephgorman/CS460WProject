package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MainMenuController implements SceneController
{
	public Button patientActionsButton;
	public Button acpButton;
	public Button logoutButton;
	
	public void doLogout(ActionEvent event)
	{
		//TODO create logout function
		Client.singleton.logout();
	}
	
	public void goToACP(ActionEvent event)
	{
		//TODO create action to get to ACP Window
		Client.singleton.setACP();
	}
	
	public void goToPatientActions(ActionEvent event){
		//TODO create actions to get to patient actions window
		Client.singleton.setPatientActions();
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
