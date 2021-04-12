package fxml;

import JavaSrc.Client;
import JavaSrc.Data.PatientInfo;
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

public class PatientActionsController implements SceneController
{
	public VBox vBox;
	public Button createChartButton;
	public Button viewChartsButton;
	public Button backButton;
	private boolean busy = false;
	
	@FXML
	public Label error;
	
	public void refresh()
	{
		Client.singleton.runCommand("loadpat");
		busy = true;
	}
	
	public void createChart(ActionEvent event)
	{
		if(busy) return;
		Client.singleton.setCreateMedicalChart();
	}
	
	public void viewChart(ActionEvent event)
	{
		if(busy) return;
	}
	
	public void back()
	{
		if(busy) return;
		Client.singleton.setMainMenu();
	}
	
	public void load(PatientInfo info)
	{
		Platform.runLater(() ->
		{
			HBox hBox = new HBox();
			Label nameLabel = new Label(info.name);
			hBox.getChildren().add(nameLabel);
			hBox.setSpacing(10);
			nameLabel.setFont(new Font(16));
			hBox.setAlignment(Pos.CENTER);
			
			Button edit = new Button("Edit");
			edit.setOnAction(e ->
			{
				if(busy) return;
				Client.singleton.runCommand("editpat " + info.id);
			});
			
			hBox.getChildren().add(edit);
			
			
			Button delete = new Button("Delete");
			delete.setOnAction(e ->
			{
				if(busy) return;
				Client.singleton.setConfDel(() ->
				{
					Client.singleton.runCommand("deletepat " + info.id);
					Client.singleton.runCommand("loadpat");
				}, () ->
						Client.singleton.runCommand("loadpat"));
			});
			
			
			hBox.getChildren().add(delete);
			vBox.getChildren().add(hBox);
			System.out.println(info);
		});
	}
	
	@Override
	public void clear()
	{
		Platform.runLater(() ->
		{
			vBox.getChildren().clear();
			error.setText("");
			busy = false;
		});
	}
	
	@Override
	public void error(String msg)
	{
		error.setText(msg);
		busy = false;
	}
	
	@Override
	public void updateInfo(UserInfo info)
	{
	
	}
}
