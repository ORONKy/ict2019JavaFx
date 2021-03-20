/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 *
 * @author varot
 */
public class CreateTournament {
	private final String WINDOW_TITLE = "Create Tournament";
	private final int WINDOW_MIN_WIDTH = 300;
	private final int WINDOW_MIN_HEIGHT = 200;

	private Label titleLabel;
	private Label gameLabel;
	private Label sizeLabel;

	private TextField titleTextField;
	private String errorMessage;

	private Button createBtn;

	private ComboBox gameDropdown;
	private ObservableList gamesList;

	private Spinner<Integer> sizeSpinner;

	private GridPane gridPane;


	public CreateTournament ( Stage primaryStage)
	{
		initWindow();

		gridPane = new GridPane();
		gridPane.add( titleLabel, 0, 0 );
		gridPane.add( titleTextField, 1,0 );
		gridPane.add( gameLabel, 0, 1 );
		gridPane.add( gameDropdown, 1, 1 );
		gridPane.add( sizeLabel, 0, 2 );
		gridPane.add( sizeSpinner, 1, 2 );

		Scene scene = new Scene( gridPane );
		Stage stage = new Stage();
		stage.setMinHeight( WINDOW_MIN_HEIGHT );
		stage.setMinWidth( WINDOW_MIN_WIDTH );
		stage.setTitle( WINDOW_TITLE );
		stage.initModality( Modality.WINDOW_MODAL );
		stage.setResizable( false );
		stage.initOwner( primaryStage );
		stage.setScene( scene );
		stage.show();
	}

	private void initWindow(){
		createBtn = new Button("Create");
		titleLabel = new Label("Title");
		gameLabel = new Label("Game");
		sizeLabel = new Label("Size");

		titleTextField = new TextField("");

		gameDropdown = new ComboBox();
		gameDropdown.getItems().addAll( List.of("fisch", "csgo") );

		sizeSpinner = new Spinner<>();
	}



	public void addActions( EventHandler<ActionEvent> evh) {
		createBtn.setOnAction( evh );
	}
}
