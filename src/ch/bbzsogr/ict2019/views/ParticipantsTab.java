/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.Tournament;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.List;

/**
 * @author varot
 */
public class ParticipantsTab extends Tab
{
	private GridPane gridPane;
	private Label title;
	private Label participantsCount;
	private Label tournamentSizeCount;
	private Label participants;
	private Label tournamentSize;
	private TableView tableView;
	private ObservableList<Participant> observableList;
	private Tournament tournament;
	private Button addParticipantsBtn;
	private Button fillRandomBtn;
	private Button editBtn;
	private Button removeBtn;

	public ParticipantsTab ( Tournament tournament )
	{
		this.tournament = tournament;
		initTab();
		initTable();
		gridPane = new GridPane();
		editBtn.disableProperty().bind( Bindings.isEmpty( tableView.getSelectionModel().getSelectedItems()));

		gridPane.add( title, 0, 0,4, 1  );
		gridPane.add( participantsCount, 0, 1, 2, 1 );
		gridPane.add( tournamentSizeCount, 3, 1, 2, 1 );
		gridPane.add( participants, 0, 2, 2, 1 );
		gridPane.add( tournamentSize, 3, 2, 2, 1 );
		gridPane.add( tableView, 0, 3, 4,1 );
		gridPane.add( addParticipantsBtn, 0, 4 );
		gridPane.add( fillRandomBtn, 1, 4 );
		gridPane.add( editBtn, 2, 4 );
		gridPane.add( removeBtn, 3, 4 );

		setClosable( false );
		setContent( gridPane );
	}

	private void initTab ()
	{
		title = new Label( "Participants of\"" + tournament.getTitle() + "\"" );
		title.setFont( Font.font( 30 ) );
		participantsCount = new Label();
		tournamentSizeCount = new Label( String.valueOf( tournament.getTournamentSize() ) );
		tournamentSizeCount.setFont( Font.font(60) );

		participants = new Label( "Participants" );
		tournamentSize = new Label( "Tournament Size" );

		addParticipantsBtn = new Button( "+ Participants" );
		fillRandomBtn = new Button( "Fill Random" );
		editBtn = new Button( "Edit" );
		removeBtn = new Button( "Remove" );
	}

	private void initTable ()
	{
		tableView = new TableView<>();
		tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

		gridPane = new GridPane();

		TableColumn<Integer, Participant> column1 = new TableColumn<>( "Name" );
		column1.setCellValueFactory( new PropertyValueFactory<>( "name" ) );

		tableView.getColumns().add( column1 );
		tableView.setMaxHeight( 150 );
	}

	public void addActions ( EventHandler<ActionEvent> evh )
	{
		addParticipantsBtn.setOnAction( evh );
		fillRandomBtn.setOnAction( evh );
		editBtn.setOnAction( evh );
		removeBtn.setOnAction( evh );
	}

	public void populateTable ( List<Participant> list )
	{
		observableList = FXCollections.observableArrayList( list );
		tableView.setItems( observableList );
		participantsCount.setText( String.valueOf( list.size() ) );
		participantsCount.setFont( Font.font( 60 ) );
	}

	public Participant getSelectedParticipant(){
		return (Participant) tableView.getSelectionModel().getSelectedItem();
	}

	public Button getAddParticipantsBtn ()
	{
		return addParticipantsBtn;
	}

	public Button getEditBtn ()
	{
		return editBtn;
	}

	public Button getRemoveBtn ()
	{
		return removeBtn;
	}

	public Button getFillRandomBtn ()
	{
		return fillRandomBtn;
	}
}
