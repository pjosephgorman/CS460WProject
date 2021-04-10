package JavaSrc;

import fxml.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application
{
	public static Client singleton;
	ConnectionHandler handler;
	Scene loading, login, mainMenu, acp, usrAccounts, confDel, patientActions, medicalChart, editAccount;
	private Stage stage;
	private Scenes scene;
	boolean timeout = false;
	
	private ACPController acpController;
	private ConfirmDeleteController confirmDeleteController;
	private LoadingController loadingController;
	private LoginController loginController;
	private MainMenuController mainMenuController;
	private MedicalChartController medicalChartController;
	private UserAccountsController userAccountsController;
	private EditAccountController editAccountController;
	private PatientActionsController patientActionsController;
	
	public enum Scenes
	{
		LOADING, LOGIN, MAINMENU, ACP, USERACCOUNTS, CONFDEL, PATIENTACTIONS, MEDICALCHART, EDITACCOUNT
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		singleton = this;
		stage = primaryStage;
		handler = new ConnectionHandler(this);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Loading.fxml"));
		loading = new Scene(loader.load());
		loadingController = loader.getController();
		
		stage.setTitle("Loading...");
		stage.setScene(loading);
		
		loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		login = new Scene(loader.load(), 300, 275);
		loginController = loader.getController();
		
		handler.start(); //Placed here so necessary scenes exist for startup
		
		loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
		mainMenu = new Scene(loader.load());
		mainMenuController = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/fxml/ACP.fxml"));
		acp = new Scene(loader.load());
		acpController = loader.getController();
		
		
		loader = new FXMLLoader(getClass().getResource("/fxml/UserAccounts.fxml"));
		usrAccounts = new Scene(loader.load());
		userAccountsController = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/fxml/ConfirmDelete.fxml"));
		confDel = new Scene(loader.load());
		confirmDeleteController = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/fxml/PatientActions.fxml"));
		patientActions = new Scene(loader.load());
		patientActionsController = loader.getController();
		
		
		loader = new FXMLLoader(getClass().getResource("/fxml/MedicalChart.fxml"));
		medicalChart = new Scene(loader.load());
		medicalChartController = loader.getController();
		
		loader = new FXMLLoader(getClass().getResource("/fxml/EditAccountInfo.fxml"));
		editAccount = new Scene(loader.load());
		editAccountController = loader.getController();
		
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
		                  	  setScene(Scenes.LOGIN);
			                  stage.setTitle("Login");
		                  });
	}
	
	public void setMainMenu()
	{
		Platform.runLater(() ->
		                  {
		                  	  setScene(Scenes.MAINMENU);
		                  	  stage.setTitle("Main Menu");
		                  });
	}
	
	public void setACP()
	{
		Platform.runLater(() ->
		                   {
		                   	   setScene(Scenes.ACP);
		                   	   stage.setTitle("Admin Control Panel");
		                   });
	}
	
	public void setPatientActions()
	{
		Platform.runLater(() ->
							{
								setScene(Scenes.PATIENTACTIONS);
								stage.setTitle("Patient Actions");
							});
	}
	
	public void setEditAccount()
	{
		Platform.runLater(() ->
		                  {
			                  setScene(Scenes.EDITACCOUNT);
			                  stage.setTitle("Edit Account");
		                  });
	}
	
	public void setCreateMedicalChart()
	{
		Platform.runLater(() ->
		                  {
			                  setScene(Scenes.MEDICALCHART);
			                  stage.setTitle("Create Medical Chart");
		                  });
	}
	
	void error(String msg)
	{
		Platform.runLater(() ->
		{
			SceneController c = getController(scene);
			
			if(c != null)
				c.error(msg);
		});
	}
	
	void clearError()
	{
		error("");
	}
	
	private SceneController getController(Scenes sc)
	{
		if(sc == null) return null;
		switch(sc)
		{
			case PATIENTACTIONS -> {return patientActionsController;}
			case LOADING -> {return loadingController;}
			case LOGIN -> {return loginController;}
			case CONFDEL -> {return confirmDeleteController;}
			case MEDICALCHART -> {return medicalChartController;}
			case ACP -> {return acpController;}
			case MAINMENU -> {return mainMenuController;}
			case USERACCOUNTS -> {return userAccountsController;}
			case EDITACCOUNT -> {return  editAccountController;}
		}
		return null;
	}
	
	public void setScene(Scenes sc)
	{
		if(sc == null) return;
		clearScene(scene);
		stage.setScene(getScene(sc));
		scene = sc;
	}
	
	private Scene getScene(Scenes sc)
	{
		if(sc == null) return null;
		switch(sc)
		{
			case PATIENTACTIONS -> {return patientActions;}
			case LOADING -> {return loading;}
			case LOGIN -> {return login;}
			case CONFDEL -> {return confDel;}
			case MEDICALCHART -> {return medicalChart;}
			case ACP -> {return acp;}
			case MAINMENU -> {return mainMenu;}
			case USERACCOUNTS -> {return usrAccounts;}
			case EDITACCOUNT -> {return editAccount;}
		}
		return null;
	}
	
	private void clearScene(Scenes sc)
	{
		SceneController c = getController(sc);
		if(c != null)
			c.clear();
	}
	
	public void logout()
	{
		setScene(Scenes.LOADING);
		handler.kill();
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
