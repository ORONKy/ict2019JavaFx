/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Tournament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author varot
 */
public class CreateTournament {
	private final String WINDOW_TITLE = "Create Tournament";
	private final int WINDOW_MIN_WIDTH = 500;
	private final int WINDOW_MIN_HEIGHT = 250;

	private Label titleLabel;
	private Label gameLabel;
	private Label sizeLabel;

	private TextField titleTextField;
	private Label errorTextField;

	private Button createBtn;

	private ComboBox gameDropdown;
	private ObservableList<Game> gamesList;

	private Spinner<Integer> sizeSpinner;

	private GridPane gridPane;

	private Stage stage;


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
		gridPane.add(errorTextField, 0, 3, 2,1);
		gridPane.add( createBtn, 1, 4 );

		gridPane.setHgap( 30 );
		gridPane.setVgap( 15 );
		GridPane.setHalignment(createBtn, HPos.RIGHT);

		Scene scene = new Scene( gridPane );
		stage = new Stage();
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
		errorTextField = new Label("");
		errorTextField.setTextFill( Color.RED);

		gameDropdown = new ComboBox();

		sizeSpinner = new Spinner<>();
		SpinnerValueFactory<Integer> sizeSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory( 2, 100, 1 );
		sizeSpinner.setValueFactory( sizeSpinnerValueFactory );
	}

	public void addActions( EventHandler<ActionEvent> evh) {
		createBtn.setOnAction( evh );
	}

	public void populateGames(List<Game> games){
		gamesList = FXCollections.observableArrayList(games);
		gameDropdown.getItems().addAll( gamesList);
		Callback<ListView<Game>, ListCell<Game>> cellFactory = new Callback<ListView<Game>, ListCell<Game>>() {
			@Override
			public ListCell<Game> call(ListView<Game> l) {
				return new ListCell<Game>() {
					@Override
					protected void updateItem(Game item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setGraphic(null);
						} else {
							setText(item.getName());
						}
					}
				} ;
			}
		};
		gameDropdown.setCellFactory( cellFactory );
		gameDropdown.setButtonCell(cellFactory.call(null));
	}

	public Game getSelectedGame(){
		return (Game) gameDropdown.getValue();
	}

	public String getTournamentName(){
		return titleTextField.getText();
	}

	public int getTournamentSize(){
		return sizeSpinner.getValue();
	}

	public Button getCreateBtn(){
		return createBtn;
	}

	public void setError(String error){
		errorTextField.setText( error );
	}

	public void closeWindow(){
		stage.close();
	}
}
