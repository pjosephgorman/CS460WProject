package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ConfirmDeleteController implements SceneController
{
	public Button yesButton;
	public Button noButton;
	private Runnable onYes, onNo;
	private String error = "";
	private boolean isDone = true;
	
	public void doYesButton(ActionEvent event)
	{
		onYes.run();
		if(!error.equals(""))
			Client.singleton.runCommand("echo error %d %s".formatted(Integer.parseInt(error.substring(2, 5)), error.substring(6)));
		clear();
	}
	
	public void doNoButton(ActionEvent event)
	{
		onNo.run();
		if(!error.equals(""))
			Client.singleton.runCommand("echo error %d %s".formatted(Integer.parseInt(error.substring(2, 5)), error.substring(6)));
		clear();
	}
	
	@Override
	public void clear()
	{
		onYes = null;
		onNo = null;
		error = "";
		isDone = true;
	}
	
	@Override
	public void error(String msg)
	{
		if(isDone && !msg.equals(""))
			Client.singleton.runCommand("echo error %d %s".formatted(Integer.parseInt(msg.substring(2, 5)), msg.substring(6)));
		else error = msg;
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
	
	public void setRunnables(Runnable yes, Runnable no)
	{
		this.onYes = yes;
		this.onNo = no;
		isDone = false;
	}
}
