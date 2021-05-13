/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import ch.bbzsogr.ict2019.db.DbConnector;
import ch.bbzsogr.ict2019.export.ExportTournament;
import ch.bbzsogr.ict2019.model.*;
import ch.bbzsogr.ict2019.util.FillRandomUtil;
import ch.bbzsogr.ict2019.util.MatchesUtil;
import ch.bbzsogr.ict2019.util.ValidationUtil;
import ch.bbzsogr.ict2019.views.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author varot
 */
public class Controller implements EventHandler<ActionEvent>
{

	private Model model;
	private TournamentList tournamentListView;
	private CreateTournament createTournamentView;
	private TournamentOverview tournamentOverviewView;
	private Stage primaryStage;
	private DbConnector db;
	private ParticipantsTab participantsTab;
	private EditParticipants editParticipants;
	private MatchTab matchTab;
	private Map<Integer, StageTab> stagesTabs;
	private FileChoserWindow fileChooserView;

	public Controller ( Stage primaryStage )
	{
		stagesTabs = new HashMap<>();
		this.primaryStage = primaryStage;
		try
		{
			db = new DbConnector();
		}
		catch ( SQLException throwables )
		{
			throwables.printStackTrace();
			Dialogs.createErrorAlert( "Db was not found. Start the db and restart the program" );
			System.exit( -1 );
		}
		createTournamentListView();
	}

	private void createTournamentListView ()
	{
		List<Tournament> tournamentList = db.readTournament();
		tournamentListView = new TournamentList( primaryStage );
		tournamentListView.populateTable( tournamentList );
		tournamentListView.addActions( this );
	}

	private void createCreateTournamentView ()
	{
		createTournamentView = new CreateTournament( primaryStage );
		createTournamentView.addActions( this );
		createTournamentView.populateGames( db.readeGames() );
	}

	@Override
	public void handle ( ActionEvent event )
	{
		Button source = (Button) event.getSource();
		if ( source == this.tournamentListView.getViewBtn() )
		{
			createTournamentOverview();
		}
		else if ( source == this.tournamentListView.getAddTournamentBtn() )
		{
			createCreateTournamentView();
		}
		else if ( this.createTournamentView != null && source == this.createTournamentView.getCreateBtn() )
		{
			createNewTournament();
		}
		else if ( this.participantsTab != null && source == this.participantsTab.getEditBtn() )
		{
			createEditParticipantView();
		}
		else if ( this.editParticipants != null && source == this.editParticipants.getSaveBtn() )
		{
			if ( editParticipants.getEditedParticipant().isNewUser() == false )
			{
				editParticipant();
			}
			else
			{
				createParticipants();
			}
		}
		else if ( this.editParticipants != null && source == this.editParticipants.getCancelBtn() )
		{
			editParticipants.closeWindow();
		}
		else if ( this.participantsTab != null && source == this.participantsTab.getAddParticipantsBtn() )
		{
			createCreateParticipantView();
		}
		else if ( this.participantsTab != null && source == this.participantsTab.getRemoveBtn() )
		{
			removeParticipant();
		}
		else if ( this.participantsTab != null && source == this.participantsTab.getFillRandomBtn() )
		{
			fillRandom();
		}
		else if ( this.matchTab != null && source == this.matchTab.getStartTournament() )
		{
			startTournament();
		}
		else if ( this.tournamentOverviewView != null && source == this.tournamentOverviewView.getExportBtn() )
		{
			tournamentExport();
		}
		else
		{
			handleMatchesButton( source );
		}
      /*  if ( source == this.view.getCountButton() ) {
            this.model.increase();
            this.view.updateLabel( this.model.getCount());
        }*/
	}

	private void handleMatchesButton ( Button source )
	{
		if ( stagesTabs == null )
			return;
		List<Match> allMatches = stagesTabs.entrySet().stream()
				.map( map -> map.getValue().getTournamentStage().getMatches() ).flatMap( List::stream )
				.collect( Collectors.toList() );
		for ( Match match : allMatches )
		{
			if ( match.getParticipant1los() == source )
			{
				setMatchWinner( match.getStageNr(), match.getParticipant2(), match.getId() );
			}
			else if ( match.getParticipant1win() == source )
			{
				setMatchWinner( match.getStageNr(), match.getParticipant1(), match.getId() );
			}
			else if ( match.getParticipant2los() == source )
			{
				setMatchWinner( match.getStageNr(), match.getParticipant1(), match.getId() );
			}
			else if ( match.getParticipant2win() == source )
			{
				setMatchWinner( match.getStageNr(), match.getParticipant2(), match.getId() );
			}
		}
	}

	private void setMatchWinner ( int stage, Participant winnerParticipant, int matchId )
	{
		try
		{
			db.setMatchWinner( winnerParticipant.getId(), matchId );
			Tournament selectedTournament = tournamentListView.getSelectedTournament();
			List<Match> dbMatches = db.readMatches( selectedTournament.getId(), stage );
			boolean stageFinished = dbMatches.stream().filter( p -> p.getWinnerParticipantId() != null ).count() > 0;
			stagesTabs.get( stage )
					.refreshStage( new TournamentStage( dbMatches, stage, stageFinished, MatchesUtil.getTournamentParticipants( dbMatches) ) );
			stagesTabs.get( stage ).addActions( this );
			if ( stageFinished )
			{
				List<Participant> stageWinner = db.readStageWinner( selectedTournament.getId(), stage );
				List<Match> stageMatches = db.readMatches( selectedTournament.getId(), stage );
				if ( stageMatches.size() == stageWinner.size() && stageWinner.size() > 1 )
				{
					List<Match> newMatches = MatchesUtil
							.createMatches( stageWinner, selectedTournament.getId(), stage );
					db.addAllMatches( selectedTournament.getId(), newMatches, stage + 1 );
					db.writeTournamentStage( selectedTournament.getId(), stage + 1 );
					createAndAddStageTabToMatchTab( selectedTournament.getId(), stage + 1, false );

				}
				else
				{
					if ( !stageWinner.isEmpty() )
						db.writeMatchWinner( stageWinner.get( 0 ).getId(), selectedTournament.getId() );
				}
			}
		}
		catch ( SQLException throwables )
		{
			throwables.printStackTrace();
		}
	}

	private void createTournamentOverview ()
	{
		Tournament selectedTournament = tournamentListView.getSelectedTournament();
		participantsTab = new ParticipantsTab( selectedTournament );
		participantsTab.addActions( this );
		participantsTab.populateTable( db.readParticipantsForTournament( selectedTournament.getId() ) );
		matchTab = new MatchTab();
		matchTab.addActions( this );
		try
		{
			int stage = db.readTournamentState( selectedTournament.getId() );
			if ( stage != 0 )
			{
				for ( int i = 0; i < stage; i++ )
				{
					createAndAddStageTabToMatchTab( selectedTournament.getId(), i + 1, i + 1 == stage );
				}
			}

		}
		catch ( SQLException throwables )
		{
			throwables.printStackTrace();
		}
		tournamentOverviewView = new TournamentOverview( primaryStage, selectedTournament, participantsTab, matchTab );
		tournamentOverviewView.addAction( this );

	}

	/**
	 * Create an Stage tab and add it to the Matches Tab
	 * Takes all Matches from a stage
	 *
	 * @param tournamentId the id of the tournament
	 * @param stageId      the stage nr
	 * @param isFinished   true if the stage is finished
	 */
	private void createAndAddStageTabToMatchTab ( int tournamentId, int stageId, boolean isFinished )
	{
		createAndAddStageTabToMatchTab( stageId, isFinished, db.readMatches( tournamentId, stageId ) );
	}

	/**
	 * Create an Staage tab and add it to the Matches Tab
	 * Takes custom Matches
	 *
	 * @param stageId    the Stage nr
	 * @param isFinished true if the stage is finished
	 * @param matches    the matches which are part of the stage
	 */
	private void createAndAddStageTabToMatchTab ( int stageId, boolean isFinished, List<Match> matches )
	{
		StageTab stageTab = MatchesUtil.createStageTab( stageId, matches, isFinished );
		matchTab.addTab( stageTab );
		stagesTabs.put( stageId, stageTab );
		stageTab.addActions( this );
	}

	private void createNewTournament ()
	{
		Game selectedGame = createTournamentView.getSelectedGame();
		String name = createTournamentView.getTournamentName();
		int size = createTournamentView.getTournamentSize();
		if ( ValidationUtil.validateCreateTournament( name, size, selectedGame ) )
		{
			try
			{
				db.createTournament( name, size, selectedGame.getId(), 0 );
				//TODO eventuel nicht erneuter db zugriff
				tournamentListView.populateTable( db.readTournament() );
			}
			catch ( SQLException throwables )
			{
				createTournamentView.setError( "Error while create Tournament" );
			}
			createTournamentView.closeWindow();
		}
		else
			createTournamentView.setError( "Input is not valid" );

	}

	private void createEditParticipantView ()
	{

		try
		{
			if ( db.readTournamentState( tournamentOverviewView.getTournament().getId() ) < 1 )
			{
				Participant participant = participantsTab.getSelectedParticipant();
				editParticipants = new EditParticipants( primaryStage, participant );
				editParticipants.addActions( this );
			}
			else
			{
				Dialogs.createWarningAlert( "Can't change Participant,\nbecause match is started", "Can't change" );
			}
		}
		catch ( SQLException throwables )
		{
			throwables.printStackTrace();
		}
	}

	private void editParticipant ()
	{
		Participant participant = editParticipants.getEditedParticipant();
		if ( ValidationUtil.validateUser( participant ) )
		{
			try
			{
				db.editParticipant( participant );
			}
			catch ( SQLException throwables )
			{
				editParticipants.setErrorTextField( "error while edit user" );
				return;
			}
			editParticipants.closeWindow();
			participantsTab.populateTable(
					db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ) );
			return;
		}
		else
			editParticipants.setErrorTextField( "user is not valid" );
	}

	private void createParticipants ()
	{
		Participant participant = editParticipants.getEditedParticipant();
		Tournament tournament = tournamentOverviewView.getTournament();
		if ( ValidationUtil.validateUser( participant ) )
		{
			try
			{
				Participant dbParticipant = db.readParticipant( participant );
				if ( dbParticipant == null )
				{
					db.createParticipant( participant, tournament.getId() );
				}
				else
				{
					db.addParticipantToTournament( dbParticipant.getId(), tournament.getId() );
				}
			}
			catch ( SQLException throwables )
			{
				editParticipants.setErrorTextField( "error while create user" );
				return;
			}
			editParticipants.closeWindow();
			participantsTab.populateTable( db.readParticipantsForTournament( tournament.getId() ) );
			return;
		}
		else
			editParticipants.setErrorTextField( "user is not valid" );
	}

	private void createCreateParticipantView ()
	{
		if ( tournamentOverviewView.getTournament().getTournamentSize() > db
				.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ).size() )
		{
			Participant participant = new Participant( true );
			editParticipants = new EditParticipants( primaryStage, participant );
			editParticipants.addActions( this );
			if ( matchTab != null && tournamentOverviewView.getTournament().getTournamentSize() > db
					.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ).size() )
			{
			}
		}
		else
		{
			Dialogs.createWarningAlert( "Tournament is full\nCan't add Participants", "Cant add Participant" );
		}

	}

	private void removeParticipant ()
	{
		Participant participant = participantsTab.getSelectedParticipant();
		try
		{
			if ( db.readTournamentState( tournamentOverviewView.getTournament().getId() ) < 1 )
			{
				if ( participant.isTemporary() )
				{
					db.removeParticipant( participant.getId() );
				}
				else
				{
					db.removeParticipantFromTournament( tournamentOverviewView.getTournament().getId(),
							participant.getId() );
				}
				participantsTab.populateTable(
						db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ) );
			}
			else
			{
				Dialogs.createWarningAlert( "Can't remove Participant,\nbecause match is started", "Can't remove" );
			}

		}
		catch ( SQLException throwables )
		{
			Dialogs.createErrorAlert( "Error while remove participant!!!\nError occurred while execute query" );
			throwables.printStackTrace();
		}

	}

	private void fillRandom ()
	{
		Tournament tournament = tournamentOverviewView.getTournament();

		try
		{
			if ( db.readTournamentState( tournament.getId() ) < 1 )
			{
				List<Participant> currentParticipants = db.readParticipantsForTournament( tournament.getId() );
				List<Participant> temporaryParticipants = FillRandomUtil
						.createTemporaryParticipants( tournament.getTournamentSize() - currentParticipants.size() );
				if ( tournamentOverviewView.getTournament().getTournamentSize() > db
						.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ).size() )
				{
					db.createAllParticipants( temporaryParticipants, tournament.getId() );

					participantsTab.populateTable(
							db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ) );
				}
				else
				{
					Dialogs.createWarningAlert( "Tournament is full\nCan't add Participants", "Cant add Participant" );
				}
			}
			else
			{
				Dialogs.createWarningAlert( "Can't fill wit random Participant,\nbecause match is started", "Can't fill Random" );
			}
		}
		catch ( SQLException throwables )
		{
			Dialogs.createErrorAlert(
					"Error while fill the tournament with random participants!!!\nError occurred while execute query" );
		}
	}

	private void startTournament ()
	{
		Tournament tournament = tournamentOverviewView.getTournament();
		List<Participant> participants = db.readParticipantsForTournament( tournament.getId() );
		if ( tournament.getTournamentSize() == participants.size() )
		{
			List<Match> matches = MatchesUtil.createMatches( participants, tournament.getId(), 1 );
			List<Match> dbMatches = null;
			try
			{
				db.addAllMatches( tournament.getId(), matches, 1 );
				db.updateTournamentState( tournament.getId(), 1 );
				dbMatches = db.readMatches( tournament.getId(), 1 );
			}
			catch ( SQLException throwables )
			{
				throwables.printStackTrace();
			}

			StageTab stage1 = MatchesUtil.createStageTab( 1,dbMatches,false );
			stage1.addActions( this );
			stagesTabs = new HashMap<>();
			stagesTabs.put( 1, stage1 );
			matchTab.createPrimaryTab( stagesTabs.get( 1 ) );
		}
		else
		{
			Dialogs.createErrorAlert( "Tournament cant be started because its not full" );
		}
	}

	private void tournamentExport ()
	{
		fileChooserView = new FileChoserWindow( this.primaryStage );
		File file = fileChooserView.showWindow();
		if ( file != null )
		{
			int tournamentId = tournamentOverviewView.getTournament().getId();
			boolean success = ExportTournament.export( db.readMatches( tournamentId ), file.getAbsolutePath() );
			if ( success )
			{
				Dialogs.infoAlert( "Tournament successfully exported" );
			}
			else
			{
				Dialogs.createErrorAlert( "Cant create Export" );
			}
		}
		else
		{
			Dialogs.createErrorAlert( "You need to create a *.csv file" );
		}
	}

}
