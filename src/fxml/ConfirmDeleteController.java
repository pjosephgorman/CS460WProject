package fxml;

import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ConfirmDeleteController implements SceneController
{
	public Button yesButton;
	public Button noButton;
	private Runnable onYes, onNo;
	
	public void doYesButton(ActionEvent event){
		onYes.run();
		clear();
	}
	
	public void doNoButton(ActionEvent event){
		onNo.run();
		clear();
	}
	
	@Override
	public void clear()
	{
		onYes = null;
		onNo = null;
	}
	
	@Override
	public void error(String msg)
	{
	
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
	
	public void setRunnables(Runnable yes, Runnable no)
	{
		this.onYes = yes;
		this.onNo = no;
	}
}
