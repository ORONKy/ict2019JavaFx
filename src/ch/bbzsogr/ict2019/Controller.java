/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import ch.bbzsogr.ict2019.db.DbConnector;
import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Match;
import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.Tournament;
import ch.bbzsogr.ict2019.util.FillRandomUtil;
import ch.bbzsogr.ict2019.util.MatchesUtil;
import ch.bbzsogr.ict2019.util.ValidationUtil;
import ch.bbzsogr.ict2019.views.*;

import java.sql.SQLException;
import java.util.List;


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

	public Controller ( Stage primaryStage )
	{
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
		else if ( this.matchTab != null && source == this.matchTab.getStartTournament())
		{

		}
      /*  if ( source == this.view.getCountButton() ) {
            this.model.increase();
            this.view.updateLabel( this.model.getCount());
        }*/
	}

	private void createTournamentOverview ()
	{
		Tournament selectedTournament = tournamentListView.getSelectedTournament();
		participantsTab = new ParticipantsTab( selectedTournament );
		participantsTab.addActions( this );
		participantsTab.populateTable( db.readParticipantsForTournament( selectedTournament.getId() ) );
		tournamentOverviewView = new TournamentOverview( primaryStage, selectedTournament, participantsTab );
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
		Participant participant = participantsTab.getSelectedParticipant();
		editParticipants = new EditParticipants( primaryStage, participant );
		editParticipants.addActions( this );
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
		if ( tournamentOverviewView.getTournament().getTournamentSize() > db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ).size() )
		{
			Participant participant = new Participant( true );
			editParticipants = new EditParticipants( primaryStage, participant );
			editParticipants.addActions( this );
		}else {
			Dialogs.createWarningAlert( "Tournament is full", "Cant add Participant" );
		}

	}

	private void removeParticipant ()
	{
		Participant participant = participantsTab.getSelectedParticipant();
		try
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
		}
		catch ( SQLException throwables )
		{
			Dialogs.createErrorAlert( "Error while remove participant!!!\nError occurred while execute query" );
			throwables.printStackTrace();
		}
		participantsTab.populateTable(
				db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ) );
	}

	private void fillRandom ()
	{
		Tournament tournament = tournamentOverviewView.getTournament();
		List<Participant> currentParticipants = db.readParticipantsForTournament( tournament.getId() );
		List<Participant> temporaryParticipants = FillRandomUtil.createTemporaryParticipants( tournament.getTournamentSize() - currentParticipants.size() );
		try
		{
			db.createAllParticipants( temporaryParticipants, tournament.getId() );
		}
		catch ( SQLException throwables )
		{

			Dialogs.createErrorAlert( "Error while fill the tournament with random participants!!!\nError occurred while execute query" );
		}
		participantsTab.populateTable(
				db.readParticipantsForTournament( tournamentOverviewView.getTournament().getId() ) );
	}

	private void startTournament()
	{
		Tournament tournament = tournamentOverviewView.getTournament();
		List<Participant> participants = db.readParticipantsForTournament( tournament.getId() );
		List<Match> matches = MatchesUtil.createMatches( participants, tournament.getId(), 1);

	}
}
