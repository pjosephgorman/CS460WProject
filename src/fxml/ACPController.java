package fxml;

import JavaSrc.Client;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ACPController implements SceneController
{
	public VBox vBox;
	public Button newUserButton;
	public Button editButton;
	public Button deleteButton;
	public Button backButton;
	
	public void createNewUser(ActionEvent event){
		Client.singleton.setUsrAccounts();
	}
	
	public void editUserInfo(ActionEvent event){
	
	}
	
	public void deleteUser(ActionEvent event){
		Client.singleton.setConfDel();
	}
	
	public void back(ActionEvent event){
		Client.singleton.setMainMenu();
	}
	
	public void load(UserInfo info){
		HBox hBox = new HBox();
		Label nameLabel = new Label(info.uname + " (" + info.fname + (info.mname == null ? "" : " " + info.mname) + " " + info.lname + ")");
		hBox.getChildren().add(nameLabel);
		Button edit = new Button("Edit");
		edit.setOnAction(e ->{
			Client.singleton.runCommand("edituser " + info.id);
		});
		hBox.getChildren().add(edit);
		Button delete = new Button("Delete");
		delete.setOnAction(e ->{
			Client.singleton.runCommand("deleteuser " + info.id);
		});
		hBox.getChildren().add(delete);
		vBox.getChildren().add(hBox);
		System.out.println(info);
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
		for(int i = 0; i< 100; i++){
			load(info);
		}
	}
}
