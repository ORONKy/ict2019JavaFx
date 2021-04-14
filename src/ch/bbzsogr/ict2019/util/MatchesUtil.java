package ch.bbzsogr.ict2019.util;

import ch.bbzsogr.ict2019.model.Match;
import ch.bbzsogr.ict2019.model.Participant;
import ch.bbzsogr.ict2019.model.TournamentStage;
import ch.bbzsogr.ict2019.views.StageTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchesUtil
{
	private void createStage( List<Participant> participantList ){

	}

	public static List<Match> createMatches(List<Participant> participantList, int tournamentId, int stageNr)
	{
		List<Match> returnValue = new ArrayList<>();
		List<Participant> participants = new ArrayList<>(participantList);

		while ( participants.size() > 0 )
		{
			if ( participants.size() > 1 )
			{
				Participant participant1 = removeRandom( participants );
				Participant participant2 = removeRandom( participants );
				returnValue.add( new Match( 0, tournamentId, participant1, participant2, stageNr, 0, null  ) );
			}
		}
		return returnValue;
	}

	private static <T> T removeRandom(List<T> list)
	{
		Random random = new Random();
		if ( list != null && list.size() > 0 )
		{
			int index = random.nextInt(list.size());
			T obj = list.remove( index );
			return obj;
		}
		return null;
	}

	public static StageTab createStageTab(int stage, List<Match> matches, boolean finished)
	{
		TournamentStage stageObj = new TournamentStage( matches, stage, finished, 5 );
		StageTab stageTab = new StageTab( stageObj );
		stageTab.setText( "stage " + stage );
		return stageTab;
	}
}
