package fxml;

import JavaSrc.Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class PatientActionsController implements SceneController
{
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
	
	@Override
	public void clear()
	{
	
	}
	
	@Override
	public void error(String msg)
	{
	
	}
}
