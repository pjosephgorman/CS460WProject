package JavaSrc;

import fxml.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application
{
	public static Client singleton;
	private ConnectionHandler handler;
	Scene loading, login, mainMenu, acp, usrAccounts, confDel, patientActions, medicalChart;
	private Stage stage;
	
	private LoginController loginController;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		singleton = this;
		stage = primaryStage;
		handler = new ConnectionHandler(this);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Loading.fxml"));
		loading = new Scene(loader.load());
		//loadingController = loader.getController();
		
		stage.setTitle("Loading...");
		stage.setScene(loading);
		
		loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		login = new Scene(loader.load(), 300, 275);
		loginController = loader.getController();
		
		handler.start(); //Placed here so necessary scenes exist for startup
		
		loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
		mainMenu = new Scene(loader.load());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/ACP.fxml"));
		acp = new Scene(loader.load());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/UserAccounts.fxml"));
		usrAccounts = new Scene(loader.load());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/ConfirmDelete.fxml"));
		confDel = new Scene(loader.load());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/PatientActions.fxml"));
		patientActions = new Scene(loader.load());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/MedicalChart.fxml"));
		medicalChart = new Scene(loader.load());
		
		stage.setOnCloseRequest(windowEvent ->
		                        {
			                        singleton = null;
			                        Platform.exit();
			                        System.exit(0);
		                        });
		stage.show();
	}
	
	void setLogin()
	{
		Platform.runLater(() ->
		                  {
			                  loginController.clear();
			                  stage.setScene(login);
			                  stage.setTitle("Login");
		                  });
	}
	
	void clearLogin()
	{
		Platform.runLater(() -> loginController.clear());
	}
	
	public void runCommand(String cmd)
	{
		handler.runCommand(cmd);
	}
	
	public static void main(String[] args)
	{
		if(singleton != null) singleton.stage.close();
		launch(args);
	}
}
