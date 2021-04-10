package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MainMenuController implements SceneController
{
	public Button patientActionsButton;
	public Button acpButton;
	public Button logoutButton;
	public Button ediAccountButton;
	
	public void doLogout(ActionEvent event)
	{
		Client.singleton.logout();
	}
	
	public void goToACP(ActionEvent event)
	{
		Client.singleton.setACP();
	}
	
	public void goToPatientActions(ActionEvent event)
	{
		Client.singleton.setPatientActions();
	}
	
	public void goToEditAccount(ActionEvent event)
	{
		Client.singleton.setEditAccount();
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
