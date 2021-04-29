package fxml;

import JavaSrc.Client;
import JavaSrc.Data.PatientInfo;
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

public class PatientActionsController implements SceneController
{
	public VBox vBox;
	private boolean busy = false;
	private boolean showDischarge = false;
	
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
			edit.setAlignment(Pos.CENTER_RIGHT);
			edit.setOnAction(e ->
			{
				if(busy) return;
				Client.singleton.setViewChart(new String[]{info.physician, info.name, String.valueOf(info.age), info.height, info.sex, info.weight,
												info.symptoms, info.vitals, info.test, info.nursecomment});
			});
			
			hBox.getChildren().add(edit);
			
			
			Button delete = new Button("Delete");
			delete.setAlignment(Pos.CENTER_RIGHT);
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
			
			if(showDischarge)
			{
				Button discharge = new Button("Discharge");
				discharge.setAlignment(Pos.CENTER_RIGHT);
				discharge.setOnAction(e ->
				{
					if(busy) return;
					Client.singleton.setPatientDischarge(info);
					busy = true;
				});
				hBox.getChildren().add(discharge);
			}
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
		showDischarge = (info.role == Roles.Nurse || info.role == Roles.Admin || info.role == Roles.Billing);
	}
}
