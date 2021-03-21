/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import ch.bbzsogr.ict2019.db.DbConnector;
import ch.bbzsogr.ict2019.model.Game;
import ch.bbzsogr.ict2019.model.Tournament;
import ch.bbzsogr.ict2019.util.ValidationUtil;
import ch.bbzsogr.ict2019.views.CreateTournament;
import ch.bbzsogr.ict2019.views.TournamentList;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 *
 * @author varot
 */
public class Controller implements EventHandler<ActionEvent> {

    private Model model;
    private TournamentList tournamentListView;
    private CreateTournament createTournamentView;
    private Stage primaryStage;
    private DbConnector db;
    
    
    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        db = new DbConnector();
        createTournamentListView();
        db = new DbConnector();
    }
    
    private void createTournamentListView(){
        List<Tournament> tournamentList = db.readTournament();
        tournamentListView = new TournamentList(primaryStage);
        tournamentListView.populateTable(tournamentList);
        tournamentListView.addActions( this );
    }

    private void createCreateTournamentView(){
        createTournamentView = new CreateTournament( primaryStage );
        createTournamentView.addActions( this );
        createTournamentView.populateGames( db.readeGames() );
    }
    
    @Override
    public void handle(ActionEvent event) {  
        Button source = (Button) event.getSource();
        if ( source == this.tournamentListView.getViewBtn() )
        {

        }else if ( source == this.tournamentListView.getAddTournamentBtn() ){
            createCreateTournamentView();
        } else if ( this.createTournamentView != null && source == this.createTournamentView.getCreateBtn() )
        {
            createNewTournament();
        }
      /*  if ( source == this.view.getCountButton() ) {
            this.model.increase();
            this.view.updateLabel( this.model.getCount());
        }*/
    }

    private void createNewTournament(){
        Game selectedGame = createTournamentView.getSelectedGame();
        String name = createTournamentView.getTournamentName();
        int size = createTournamentView.getTournamentSize();
        if ( ValidationUtil.validateCreateTournament( name, size, selectedGame ) ){
            try
            {
                db.createTournament( name, size, selectedGame.getId(), 0);
            }
            catch ( SQLException throwables )
            {
                createTournamentView.setError( "Error while create Tournament" );
            }
            createTournamentView.closeWindow();
        }
        else createTournamentView.setError( "Input is not valid" );

    }
}
