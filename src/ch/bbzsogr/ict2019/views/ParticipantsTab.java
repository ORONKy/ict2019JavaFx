/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.Tournament;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author varot
 */
public class ParticipantsTab {
	private GridPane gridPane;
	private Tab tab;
	private Label title;
	private Label participantsCount;
	private Label tournamentSizeCount;
	private Label participants;
	private Label tournamentSize;
	private TableView<Participant> tableView;
	private ObservableList<Participant> observableList;
	private Tournament tournament;

	public ParticipantsTab(Tournament tournament){
		this.tournament = tournament;
	}

	private void initTab(){
		title = new Label("Participants of\""+tournament.getTitle()+"\"");
		participantsCount = new Label();
		tournamentSizeCount = new Label(String.valueOf( tournament.getTournamentSize() ));
	}

}
