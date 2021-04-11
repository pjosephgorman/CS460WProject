package fxml;

import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ConfirmDeleteController implements SceneController
{
	public Button yesButton;
	public Button noButton;
	
	public void doYesButton(ActionEvent event){
		//TODO creates function that confirms deletion
	}
	
	public void doNoButton(ActionEvent event){
		//TODO creates function that denies deletion
	}
	
	@Override
	public void clear()
	{
	
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
