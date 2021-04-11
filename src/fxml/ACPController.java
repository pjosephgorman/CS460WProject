package fxml;

import JavaSrc.Client;
import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ACPController implements SceneController
{
	public VBox vBox;
	public Button newUserButton;
	public Button editButton;
	public Button deleteButton;
	public Button backButton;
	
	@FXML
	public Label error;
	
	public void createNewUser(ActionEvent event)
	{
		Client.singleton.setUsrAccounts();
	}
	
	public void editUserInfo(ActionEvent event)
	{
	
	}
	
	public void deleteUser(ActionEvent event)
	{
	
	}
	
	public void back(ActionEvent event)
	{
		Client.singleton.setMainMenu();
	}
	
	public void load(UserInfo info)
	{
		Platform.runLater(() ->
		{
			HBox hBox = new HBox();
			Label nameLabel = new Label(info.uname + " (" + info.fname + (info.mname == null ? "" : " " + info.mname) + " " + info.lname + ")");
			hBox.getChildren().add(nameLabel);
			hBox.setSpacing(10);
			nameLabel.setFont(new Font(16));
			hBox.setAlignment(Pos.CENTER);
			
			Button edit = new Button("Edit");
			edit.setOnAction(e ->
					Client.singleton.runCommand("edituser " + info.id));
			
			hBox.getChildren().add(edit);
			
			
			Button delete = new Button("Delete");
			delete.setOnAction(e ->
					Client.singleton.setConfDel(() ->
					{
						Client.singleton.runCommand("deleteuser " + info.id);
						Client.singleton.runCommand("loadacp");
					}, () ->
							Client.singleton.runCommand("loadacp")));
			
			
			hBox.getChildren().add(delete);
			vBox.getChildren().add(hBox);
			System.out.println(info);
		});
	}
	
	@Override
	public void clear()
	{
		Platform.runLater(() -> {
			vBox.getChildren().clear();
			error.setText("");
		});
	}
	
	@Override
	public void error(String msg)
	{
		System.out.println("Set ACP error = '%s'".formatted(msg));
		error.setText(msg);
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
		if(info == null || info.role != Roles.Admin)
			clear();
	}
}
