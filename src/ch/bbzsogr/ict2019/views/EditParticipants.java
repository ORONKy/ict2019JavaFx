/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author varot
 */
public class EditParticipants {
	private final String WINDOW_TITLE = "Create Tournament";
	private final int WINDOW_MIN_WIDTH = 500;
	private final int WINDOW_MIN_HEIGHT = 250;

	private Label nameLabel;

	private TextField nameTextField;
	private Label errorTextField;
	private Label typeLabel;

	private RadioButton teamRadioButton;
	private RadioButton playerRadioButton;
	private ToggleGroup toggleGroup;

	private CheckBox temporaryCheckbox;

	private Button saveBtn;
	private Button cancelBtn;

	private GridPane gridPane;

	private Stage stage;

	private Participant participant;


	public EditParticipants ( Stage primaryStage, Participant participant)
	{
		this.participant = participant;

		initWindow();

		gridPane = new GridPane();
		gridPane.add( nameLabel, 0, 0 );
		gridPane.add( nameTextField, 1,0, 2,1 );
		gridPane.add( typeLabel, 0, 1 );
		gridPane.add( playerRadioButton, 1, 1 );
		gridPane.add( teamRadioButton, 2, 1 );
		gridPane.add( temporaryCheckbox, 0, 2, 3,1 );
		gridPane.add( errorTextField, 0, 3, 3,1 );
		gridPane.add( cancelBtn, 1,4 );
		gridPane.add( saveBtn, 2,4 );

		gridPane.setHgap( 30 );
		gridPane.setVgap( 15 );
		GridPane.setHalignment( saveBtn, HPos.RIGHT);
		gridPane.setPadding( new Insets( 10 ) );

		Scene scene = new Scene( gridPane );
		stage = new Stage();
		stage.setTitle( WINDOW_TITLE );
		stage.initModality( Modality.WINDOW_MODAL );
		stage.setResizable( false );
		stage.initOwner( primaryStage );
		stage.setScene( scene );
		stage.show();
	}

	private void initWindow(){
		saveBtn = new Button("Save");
		cancelBtn = new Button("Cancel");
		nameLabel = new Label("Title");
		nameTextField = new TextField( participant.getName() );

		toggleGroup = new ToggleGroup();
		teamRadioButton = new RadioButton("Team");
		playerRadioButton = new RadioButton("Player");
		teamRadioButton.setToggleGroup( toggleGroup );
		playerRadioButton.setToggleGroup( toggleGroup );
		if ( participant.isTeam() )
		{
			teamRadioButton.setSelected( true );
		}
		else
		{
			playerRadioButton.setSelected( true );
		}
		typeLabel = new Label("Type");

		temporaryCheckbox = new CheckBox("Temporary");
		if ( participant.isTemporary() )
		{
			temporaryCheckbox.setSelected( true );
		}
		errorTextField = new Label();
		errorTextField.setTextFill( Color.RED );
	}

	public void addActions( EventHandler<ActionEvent> evh) {
		saveBtn.setOnAction( evh );
		cancelBtn.setOnAction( evh );
	}

	public void closeWindow(){
		stage.close();
	}

	public Button getCancelBtn ()
	{
		return cancelBtn;
	}

	public Button getSaveBtn ()
	{
		return saveBtn;
	}

	public Participant getEditedParticipant(){
		return new Participant( participant.getId(), nameTextField.getText(),temporaryCheckbox.isSelected() , isTeamEdit(), participant.isNewUser());
	}

	public boolean isTeamEdit(){
		return toggleGroup.getSelectedToggle() == teamRadioButton;
	}

	public void setErrorTextField (String message )
	{
		this.errorTextField.setText( message );
	}


}
