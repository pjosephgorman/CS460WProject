package JavaSrc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application
{
	private ConnectionHandler handler;
	Scene loading, login, mainMenu, acp, usrAccounts, confDel, patientActions, medicalChart;
	Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		stage = primaryStage;
		handler = new ConnectionHandler(this);
		handler.start();
		handler.runCommand("Fuck");
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		login = new Scene(root, 300, 275);
		
		Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
		mainMenu = new Scene(mainMenuParent);
		
		Parent acpParent = FXMLLoader.load(getClass().getResource("/fxml/ACP.fxml"));
		acp = new Scene(acpParent);
		
		Parent userAccountParent = FXMLLoader.load(getClass().getResource("/fxml/UserAccounts.fxml"));
		usrAccounts = new Scene(userAccountParent);
		
		Parent confirmDeleteParent = FXMLLoader.load(getClass().getResource("/fxml/ConfirmDelete.fxml"));
		confDel = new Scene(confirmDeleteParent);
		
		Parent patientActionsParent = FXMLLoader.load(getClass().getResource("/fxml/PatientActions.fxml"));
		patientActions = new Scene(patientActionsParent);
		
		Parent medicalChartParent = FXMLLoader.load(getClass().getResource("/fxml/MedicalChart.fxml"));
		medicalChart = new Scene(medicalChartParent);
		
		stage.setTitle("Loading...");
		stage.setScene(login); //should be loading, once that's added
		stage.show();
	}
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
