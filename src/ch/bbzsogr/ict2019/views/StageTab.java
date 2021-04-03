package ch.bbzsogr.ict2019.views;

import ch.bbzsogr.ict2019.model.TournamentStage;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class StageTab extends Tab
{
	private TournamentStage tournamentStage;
	private List<GridPane> matches;
	private HBox matchesHBox;

	public StageTab ( TournamentStage tournamentStage )
	{
		this.tournamentStage = tournamentStage;
	}

	private void initTab ()
	{
	}

	private void generateMatches ()
	{

	}

	private void fillMatchGridPaneList ()
	{

	}
}
