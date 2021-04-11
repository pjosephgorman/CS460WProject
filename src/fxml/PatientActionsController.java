package fxml;

import JavaSrc.Client;
import JavaSrc.Data.PatientInfo;
import JavaSrc.Data.UserInfo;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PatientActionsController implements SceneController
{
	public VBox vBox;
	public Button createChartButton;
	public Button viewChartsButton;
	public Button backButton;
	
	public void createChart(ActionEvent event){
		Client.singleton.setCreateMedicalChart();
	}
	
	public void viewChart(ActionEvent event){
	
	}
	
	public void back(){
		Client.singleton.setMainMenu();
	}
	
	public void loadPatient(PatientInfo info){
		HBox hBox = new HBox();
		Label nameLabel = new Label(info.fname + " (" + info.fname + (info.mname == null ? "" : " " + info.mname) + " " + info.lname + ")");
		hBox.getChildren().add(nameLabel);
		hBox.setSpacing(10);
		nameLabel.setFont(new Font(16));
		hBox.setAlignment(Pos.CENTER);
		
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
	
	}
}
