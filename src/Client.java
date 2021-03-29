package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    /*
    * Main Menu Scene
    * */
    public void changeToMainMenu(ActionEvent event) throws IOException {
        Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Main Menu");
        window.setScene(mainMenuScene);
        window.show();
    }

    /*
    * ACP Scene
    * */
    public void changeToACP(ActionEvent event) throws IOException {
        Parent acpParent = FXMLLoader.load(getClass().getResource("ACP.fxml"));
        Scene acpScene = new Scene(acpParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("ACP");
        window.setScene(acpScene);
        window.show();
    }

    /*
    * ACP User Accounts edit and add
    * */
    public void changeToUserAccounts(ActionEvent event) throws IOException{
        Parent userAccountParent = FXMLLoader.load(getClass().getResource("UserAccount.fxml"));
        Scene userAccountScene = new Scene(userAccountParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Edit User Accounts");
        window.setScene(userAccountScene);
        window.show();
    }

    /*
    * Confirm Delete Scene
    * */
    public void changeToConfirmDelete(ActionEvent event) throws IOException{
        Parent confirmDeleteParent = FXMLLoader.load(getClass().getResource("ConfirmDelete.fxml"));
        Scene confirmDeleteScene = new Scene(confirmDeleteParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Confirm Delete");
        window.setScene(confirmDeleteScene);
        window.show();
    }

    /*
    * Patient Actions Scene
    * */
    public void changeToPatientActions(ActionEvent event) throws IOException{
        Parent patientActionsParent = FXMLLoader.load(getClass().getResource("PatientActions.fxml"));
        Scene patientActionsScene = new Scene(patientActionsParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Hospital Patients");
        window.setScene(patientActionsScene);
        window.show();
    }

    /*
    * Medical Chart Scene
    * */
    public void changeToMedicalChart(ActionEvent event) throws IOException{
        Parent medicalChartParent = FXMLLoader.load(getClass().getResource("MedicalChart.fxml"));
        Scene medicalChartScene = new Scene(medicalChartParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Medical Chart");
        window.setScene(medicalChartScene);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
