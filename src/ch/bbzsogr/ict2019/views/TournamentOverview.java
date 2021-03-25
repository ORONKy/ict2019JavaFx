/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Tournament;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author varot
 */
public class TournamentOverview {

	private GridPane gridPane;
	private Stage stage;
	private Tournament tournament;
	private Label gameLabel;
	private Label tournamentNameLabel;
	private Button exportBtn;
	private TabPane tabPane;
	private Tab participantTab;
	private Tab matchesTab;

	private Image img;
	private ImageView imgView;

	private final String WINDOW_TITLE = "Tournament Overview";

	public TournamentOverview ( Stage primaryStage, Tournament tournament, Tab participantTab)
	{
		this.tournament = tournament;
		this.participantTab = participantTab;

		initWindow();
		initTabPane();

		gridPane = new GridPane();
		GridPane.setHalignment(exportBtn, HPos.RIGHT);


		gridPane.add( imgView,0,0, 2, 1 );
		gridPane.add( tournamentNameLabel,0,1, 2, 1 );
		gridPane.add( gameLabel,0,2, 1, 1 );
		gridPane.add( exportBtn,1,2, 1, 1 );
		gridPane.add( tabPane, 0, 3, 2, 1 );


		Scene scene = new Scene( gridPane );
		stage = new Stage();
		stage.setTitle( WINDOW_TITLE );
		stage.initModality( Modality.WINDOW_MODAL );
		stage.sizeToScene();
		stage.setResizable( false );
		stage.initOwner( primaryStage );
		stage.setScene( scene );
		stage.sizeToScene();
		stage.show();
	}

	private void initWindow(){
		img = new Image("header.png", 341, 85, false, false);
		imgView = new ImageView(img);
		updateTournamentRelatedItems();

		exportBtn = new Button("Export");
	}

	private void initTabPane(){
		tabPane = new TabPane();
		tabPane.getTabs().add( participantTab );

		tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE);

	}

	private void updateTournamentRelatedItems(){
		gameLabel = new Label(tournament.getGame());
		gameLabel.setFont( Font.font( 40 ) );
		tournamentNameLabel = new Label( tournament.getTitle());
		tournamentNameLabel.setFont( Font.font( 55 ) );
	}
}
