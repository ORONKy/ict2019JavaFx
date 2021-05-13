/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbzsogr.ict2019.views;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author varot
 */
public class MatchTab extends Tab
{
	private Button startTournament;
	private boolean started;
	private TabPane tabPane;

	public MatchTab ()
	{
		setClosable( false );
		initStartTab();
		setContent( startTournament );
	}

	private void initStartTab ()
	{
		startTournament = new Button("Start Tournament");
		startTournament.setDisable( false );
	}

	public void createPrimaryTab(Tab tab){
		startTournament = null;
		tabPane = new TabPane();
		started = true;
		tabPane.getTabs().add( tab );
		setContent( tabPane );
	}

	public boolean isStarted ()
	{
		return started;
	}


	public void addTab(Tab tab){
		if ( tabPane != null )
		{
			tabPane.getTabs().add( tab );
		}else
		{
			tabPane = new TabPane();
			tabPane.getTabs().add( tab );
			setContent( tabPane );
		}
	}

	public Button getStartTournament ()
	{
		return startTournament;
	}

	public void addActions ( EventHandler<ActionEvent> evh )
	{
		startTournament.setOnAction( evh );
	}
}
