/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import ch.bbzsogr.ict2019.db.DbConnector;
import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.Tournament;
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

	public Controller ( Stage primaryStage )
	{
		this.primaryStage = primaryStage;
		db = new DbConnector();
		createTournamentListView();
		db = new DbConnector();
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
			editParticipant();
		}
		else if ( this.editParticipants != null && source == this.editParticipants.getCancelBtn() )
		{
			editParticipants.closeWindow();
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
		}
		else
			editParticipants.setErrorTextField( "user is not valid" );
	}

}
