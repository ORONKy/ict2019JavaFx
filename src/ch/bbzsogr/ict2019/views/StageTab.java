package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Match;
import ch.bbzsogr.ict2019.model.TournamentStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StageTab extends Tab
{
	private TournamentStage tournamentStage;
	private List<BorderedTitledPane> matches;
	private VBox matchesVBox;
	private HBox infoBox;

	private Label title;
	private Label matchesCount;
	private Label participantsCount;

	private ScrollPane scrollPane;

	public StageTab ( TournamentStage tournamentStage )
	{
		scrollPane = new ScrollPane();
		matches = new ArrayList<>();
		this.tournamentStage = tournamentStage;
		initTab();
		scrollPane.setMaxHeight( 500 );
		setClosable( false );
	}

	private void initTab ()
	{
		title = new Label( "Stage " + tournamentStage.getStageNr() );
		title.setFont( Font.font( 40 ) );
		matchesCount = new Label( tournamentStage.getMatches().size() + " Matches" );
		matchesCount.setFont( Font.font( 20 ) );
		matchesCount.setAlignment( Pos.BASELINE_CENTER );
		participantsCount = new Label( tournamentStage.getParticipantsCount() + " Participants" );
		participantsCount.setFont( Font.font( 20 ) );
		participantsCount.setAlignment( Pos.BOTTOM_CENTER );

		matchesVBox = new VBox();
		infoBox = new HBox();
		infoBox.getChildren().add( title );
		infoBox.getChildren().add( matchesCount );
		infoBox.getChildren().add( participantsCount );
		infoBox.setSpacing( 30 );
		matchesVBox.getChildren().add( infoBox );
		matchesVBox.setSpacing( 40 );
		scrollPane.setContent( matchesVBox );
		setContent( scrollPane );
		fillMatchGridPaneList( tournamentStage.getMatches() );
		generateMatches();
	}

	public void generateMatches ()
	{
		for ( BorderedTitledPane pane : matches )
		{
			matchesVBox.getChildren().add( pane );
		}
	}

	private void fillMatchGridPaneList ( List<Match> matchList )
	{
		List<Match> returnValue = new ArrayList<>();
		for ( Match match : matchList )
		{
			GridPane matchPane = new GridPane();
			Label vsLabel = new Label( "VS" );
			Label p1Name = new Label( match.getParticipant1().getName() );
			p1Name.setFont( Font.font( 20 ) );
			Label p2Name = match.getParticipant2() != null ? new Label( match.getParticipant2().getName() ) : new Label("-");
			p2Name.setFont( Font.font( 20 ) );

			if ( match.getWinnerParticipantId() == null )
			{
				HBox p1Box = new HBox();
				HBox p2Box = new HBox();
				Button p1Win = new Button( "W" );
				p1Win.setStyle( "-fx-background-color: #86d615" );
				Button p1Los = new Button( "L" );
				p1Los.setStyle( "-fx-background-color: #e80000" );
				Button p2Win = new Button( "W" );
				p2Win.setStyle( "-fx-background-color: #86d615" );
				Button p2Los = new Button( "L" );
				p2Los.setStyle( "-fx-background-color: #e80000" );

				p1Box.getChildren().add( p1Win );
				p1Box.getChildren().add( p1Los );
				p2Box.getChildren().add( p2Win );
				p2Box.getChildren().add( p2Los );
				p1Box.setSpacing( 5 );
				p2Box.setSpacing( 5 );

				matchPane.add( p1Box, 0, 0 );
				matchPane.add( p2Box, 2, 0 );

				match.setParticipant1los( p1Los );
				match.setParticipant2los( p2Los );
				match.setParticipant1win( p1Win );
				match.setParticipant2win( p2Win );
			}
			else
			{
				Label win = new Label( "W" );
				win.setTextFill( Color.GREEN );
				win.setFont( Font.font( 30 ) );
				Label lose = new Label( "L" );
				lose.setTextFill( Color.RED );
				lose.setFont( Font.font( 30 ) );

				if ( match.getWinnerParticipantId().getId() == match.getParticipant1().getId() )
				{
					matchPane.add( win, 1, 1 );
					matchPane.add( lose, 2, 1 );
				}
				else
				{
					matchPane.add( lose, 0, 1 );
					matchPane.add( win, 2, 1 );
				}

			}

			matchPane.add( p1Name, 0, 2 );
			matchPane.add( vsLabel, 1, 2 );
			matchPane.add( p2Name, 2, 2 );
			matchPane.setHgap( 15 );
			matchPane.setHgap( 5 );
			matchPane.setHgap( 30 );
			matchPane.setPadding( new Insets( 20 ) );
			BorderedTitledPane btp = new BorderedTitledPane("Match "+match.getStageNr()+"."+matchList.indexOf( match ), matchPane);
			matches.add( btp );
		}
	}

	public void refreshMatchesCount ()
	{
		matchesCount.setText( tournamentStage.getMatches().size() + " Matches" );
	}

	public TournamentStage getTournamentStage ()
	{
		return tournamentStage;
	}

	public void addActions ( EventHandler<ActionEvent> evh )
	{
		for ( Match match : tournamentStage.getMatches() )
		{
			if ( match.getParticipant1win() != null && match.getParticipant1los() != null
					&& match.getParticipant2los() != null && match.getParticipant2win() != null )
			{
				match.getParticipant2win().setOnAction( evh );
				match.getParticipant2los().setOnAction( evh );
				match.getParticipant1win().setOnAction( evh );
				match.getParticipant1los().setOnAction( evh );
			}
		}
	}

	public void refreshStage ( TournamentStage stage )
	{
		matches = new ArrayList<>();
		this.tournamentStage = stage;
		initTab();
	}
}

class BorderedTitledPane extends StackPane
{
	BorderedTitledPane(String titleString, Node content) {
		Label title = new Label("  " + titleString + "  ");
		title.getStyleClass().add("bordered-titled-title");
		StackPane.setAlignment(title, Pos.TOP_LEFT);

		StackPane contentPane = new StackPane();
		content.getStyleClass().add("bordered-titled-content");
		contentPane.getChildren().add(content);

		getStyleClass().add("bordered-titled-border");
		getChildren().addAll(title, contentPane);
	}
}
