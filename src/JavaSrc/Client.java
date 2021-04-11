package JavaSrc;

import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
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
	Scene loading, login, mainMenu, acp, usrAccounts, confDel, patientActions, medicalChart, editAccount, welcome;
	private Stage stage;
	private Scenes scene;
	boolean timeout = false;
	
	private WelcomeController welcomeController;
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
		LOADING, LOGIN, MAINMENU, ACP, USERACCOUNTS, CONFDEL, PATIENTACTIONS, MEDICALCHART, EDITACCOUNT, WELCOME
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
		
		loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
		welcome = new Scene(loader.load(), 300, 275);
		welcomeController = loader.getController();
		
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
	
	public void setWelcome(){
		Platform.runLater(() ->
		                  {
			                  setScene(Scenes.WELCOME);
			                  stage.setTitle("Login");
		                  });
	}
	
	public void setLogin()
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
		                   	   if(setScene(Scenes.ACP))
		                       {
		                       	stage.setTitle("Admin Control Panel");
		                       }
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
	
	public void setConfDel(Runnable yes, Runnable no)
	{
		if(yes == null || no == null) return;
		Platform.runLater(() ->
		                  {
			                  confirmDeleteController.setRunnables(yes, no);
			                  setScene(Scenes.CONFDEL);
			                  stage.setTitle("Confirm Delete");
		                  });
	}
	
	public void setUsrAccounts()
	{
		Platform.runLater(() ->
		                  {
			                  setScene(Scenes.USERACCOUNTS);
			                  stage.setTitle("User Accounts");
		                  });
	}
	
	public void loadACP(UserInfo info)
	{
			acpController.load(info);
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
	
	void updateInfo(UserInfo info)
	{
		Util.msg("Updating login profile with info:%n%s%n".formatted(info));
		for(Scenes sc : Scenes.values())
		{
			SceneController c = getController(sc);
			c.updateInfo(info);
		}
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
			case WELCOME -> {return welcomeController;}
		}
		return null;
	}
	
	public boolean setScene(Scenes sc)
	{
		if(sc == null) return false;
		if(sc == scene) return true;
		if(sc == Scenes.ACP && handler.getRole() != Roles.Admin) return false;
		clearScene(scene);
		stage.setScene(getScene(sc));
		scene = sc;
		return true;
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
			case WELCOME -> {return welcome;}
		}
		return null;
	}
	
	void clearScene(Scenes sc)
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
