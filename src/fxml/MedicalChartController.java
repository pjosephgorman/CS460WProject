package fxml;

import javafx.event.ActionEvent;
import javafx.scene.control.*;


public class MedicalChartController
{
	public ChoiceBox physicianChoiceBox;
	public TextField idTextField;
	public TextField nameTextField;
	public TextField ageTextField;
	public ComboBox sexComboBox;
	public TextArea symptomsTextArea;
	public TextArea testsTextArea;
	public Button editFieldButton;
	public Button testPatientButton;
	public Button cancelButton;
	
	
	public void doEdit(ActionEvent event){
		//TODO create edit feature for fields
	}
	
	public void doTest(ActionEvent event){
		//TODO create feature to perform a test
	}
	
	public void doCancel(ActionEvent event){
		//TODO create cancel feature to go back
	}
	
	public void fillTestsPerformed(ActionEvent event){
		//TODO fills the tests performed field after button is pressed
	}
}
