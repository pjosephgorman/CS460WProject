package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ACPController implements SceneController
{
	public Button newUserButton;
	public Button editButton;
	public Button deleteButton;
	public Button backButton;
	
	public void createNewUser(ActionEvent event){
	
	}
	
	public void editUserInfo(ActionEvent event){
	
	}
	
	public void deleteUser(ActionEvent event){
	
	}
	
	public void back(ActionEvent event){
		Client.singleton.setMainMenu();
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
