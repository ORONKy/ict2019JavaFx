/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019;

import ch.bbzsogr.ict2019.db.DbConnector;
import ch.bbzsogr.ict2019.model.Tournament;
import ch.bbzsogr.ict2019.views.CreateTournament;
import ch.bbzsogr.ict2019.views.TournamentList;
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
    
    @Override
    public void handle(ActionEvent event) {  
        Button source = (Button) event.getSource();
        if ( source == this.tournamentListView.getViewBtn() )
        {

        }else if ( source == this.tournamentListView.getAddTournamentBtn() ){
            createTournamentView = new CreateTournament( primaryStage );
            createTournamentView.addActions( this );
        }
      /*  if ( source == this.view.getCountButton() ) {
            this.model.increase();
            this.view.updateLabel( this.model.getCount());
        }*/
    }
    
}
